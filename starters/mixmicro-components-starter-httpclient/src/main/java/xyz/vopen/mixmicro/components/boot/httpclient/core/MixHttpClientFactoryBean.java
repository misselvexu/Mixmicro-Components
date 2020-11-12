package xyz.vopen.mixmicro.components.boot.httpclient.core;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import xyz.vopen.mixmicro.components.boot.httpclient.ErrorDecoderInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientErrorDecoder;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientLogStrategy;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.CustomOkHttpClient;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.InterceptorComponent;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.MixHttpClient;
import xyz.vopen.mixmicro.components.boot.httpclient.autoconfigure.MixHttpClientConfigBean;
import xyz.vopen.mixmicro.components.boot.httpclient.autoconfigure.MixHttpClientProperties;
import xyz.vopen.mixmicro.components.boot.httpclient.degrade.*;
import xyz.vopen.mixmicro.components.boot.httpclient.degrade.impl.SentinelDegradeInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.AbstractGlobalInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.AbstractLoggingInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.AbstractPathMatchInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.ServiceInstanceChooserInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.kits.BeanPropertiesKit;
import xyz.vopen.mixmicro.components.boot.httpclient.kits.MixHttpClientKit;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("DuplicatedCode")
public class MixHttpClientFactoryBean<T> implements FactoryBean<T>, EnvironmentAware, ApplicationContextAware {

  private static final Logger log = LoggerFactory.getLogger(MixHttpClientFactoryBean.class);

  public static final String SUFFIX = "/";
  private static final Map<Class<? extends CallAdapter.Factory>, CallAdapter.Factory> CALL_ADAPTER_FACTORIES_CACHE = new HashMap<>(4);

  private final Class<T> httpClientInterface;

  private Environment environment;

  private MixHttpClientProperties httpClientProperties;

  private MixHttpClientConfigBean httpClientConfigBean;

  private ApplicationContext applicationContext;

  private final MixHttpClient mixHttpClient;

  private static final Map<Class<? extends Converter.Factory>, Converter.Factory>
      CONVERTER_FACTORIES_CACHE = new HashMap<>(4);

  public MixHttpClientFactoryBean(Class<T> httpClientInterface) {
    this.httpClientInterface = httpClientInterface;
    mixHttpClient = httpClientInterface.getAnnotation(MixHttpClient.class);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public T getObject() throws Exception {
    checkHttpClientInterface(httpClientInterface);
    Retrofit retrofit = getRetrofit(httpClientInterface);
    // source
    T source = retrofit.create(httpClientInterface);

    MixHttpClientProperties httpClientProperties = httpClientConfigBean.getHttpClientProperties();

    // fallback class
    Class<?> fallbackClass = mixHttpClient.fallback();
    Object fallback = null;
    if (!void.class.isAssignableFrom(fallbackClass)) {
      fallback = MixHttpClientKit.getBean(applicationContext, fallbackClass);
    }

    // fallback factory class
    Class<?> fallbackFactoryClass = mixHttpClient.fallbackFactory();
    FallbackFactory<?> fallbackFactory = null;
    if (!void.class.isAssignableFrom(fallbackFactoryClass)) {
      fallbackFactory =
          (FallbackFactory) MixHttpClientKit.getBean(applicationContext, fallbackFactoryClass);
    }

    this.loadDegradeRules();

    // proxy
    return (T)
        Proxy.newProxyInstance(
            httpClientInterface.getClassLoader(),
            new Class<?>[] {httpClientInterface},
            new MixHttpClientInvocationHandler(
                source, fallback, fallbackFactory, httpClientProperties));
  }

  private void loadDegradeRules() {

    Method[] methods = httpClientInterface.getMethods();
    for (Method method : methods) {
      if (method.isDefault()) {
        continue;
      }
      int modifiers = method.getModifiers();
      if (Modifier.isStatic(modifiers)) {
        continue;
      }
      // 获取熔断配置
      MixHttpClientDegrade degrade;
      if (method.isAnnotationPresent(MixHttpClientDegrade.class)) {
        degrade = method.getAnnotation(MixHttpClientDegrade.class);
      } else {
        degrade = httpClientInterface.getAnnotation(MixHttpClientDegrade.class);
      }

      if (degrade == null) {
        continue;
      }

      DegradeStrategy degradeStrategy = degrade.degradeStrategy();
      AbstractResourceNameParser resourceNameParser = httpClientConfigBean.getResourceNameParser();
      String resourceName = resourceNameParser.parseResourceName(method, environment);

      DegradeRule degradeRule = new DegradeRule();
      degradeRule.setCount(degrade.count());
      degradeRule.setDegradeStrategy(degradeStrategy);
      degradeRule.setTimeWindow(degrade.timeWindow());
      degradeRule.setResourceName(resourceName);
      DegradeRuleInitializer.addRetrofitDegradeRule(degradeRule);
    }
  }

  /**
   * HttpClientInterface检查
   *
   * @param httpClientInterface .
   */
  @SuppressWarnings("ConstantConditions")
  private void checkHttpClientInterface(Class<T> httpClientInterface) {
    // check class type
    Assert.isTrue(httpClientInterface.isInterface(), "@MixHttpClient can only be marked on the interface type!");
    Method[] methods = httpClientInterface.getMethods();

    MixHttpClient mixHttpClient = httpClientInterface.getAnnotation(MixHttpClient.class);

    Assert.isTrue(
        StringUtils.hasText(mixHttpClient.baseUrl())
            || StringUtils.hasText(mixHttpClient.serviceId()),
        "@MixHttpClient's baseUrl and serviceId must be configured with one！");

    for (Method method : methods) {
      Class<?> returnType = method.getReturnType();
      if (method.isAnnotationPresent(CustomOkHttpClient.class)) {
        Assert.isTrue(returnType.equals(OkHttpClient.Builder.class), "For methods annotated by @OkHttpClientBuilder, the return value must be OkHttpClient.Builder .");
        Assert.isTrue(Modifier.isStatic(method.getModifiers()), "only static method can annotated by <@CustomOkHttpClient> ");
        continue;
      }

      Assert.isTrue(!void.class.isAssignableFrom(returnType), "The void keyword is not supported as the return type, please use <java.lang.Void> , method=" + method);

      if (httpClientProperties.isDisableVoidReturnType()) {
        Assert.isTrue(!Void.class.isAssignableFrom(returnType), "Configured to disable <java.lang.Void> as the return value, please specify another return type , method=" + method);
      }
    }

    Class<?> fallbackClass = mixHttpClient.fallback();
    if (!void.class.isAssignableFrom(fallbackClass)) {
      Assert.isTrue(httpClientInterface.isAssignableFrom(fallbackClass), "The fallback type must implement the current interface！the fallback type is " + fallbackClass);
      Object fallback = MixHttpClientKit.getBean(applicationContext, fallbackClass);
      Assert.notNull(fallback, "fallback  must be a valid spring bean! the fallback class is " + fallbackClass);
    }

    Class<?> fallbackFactoryClass = mixHttpClient.fallbackFactory();
    if (!void.class.isAssignableFrom(fallbackFactoryClass)) {
      Assert.isTrue(FallbackFactory.class.isAssignableFrom(fallbackFactoryClass), "The fallback factory type must implement <FallbackFactory> , the fallback factory is " + fallbackFactoryClass);
      Object fallbackFactory = MixHttpClientKit.getBean(applicationContext, fallbackFactoryClass);
      Assert.notNull(fallbackFactory, "fallback factory  must be a valid spring bean! the fallback factory class is " + fallbackFactoryClass);
    }
  }

  @Override
  public Class<T> getObjectType() {
    return this.httpClientInterface;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  /**
   * Get okhttp3 connection pool
   *
   * @param httpClientInterfaceClass httpClientInterfaceClass
   * @return okhttp3 connection pool
   */
  private synchronized okhttp3.ConnectionPool getConnectionPool(Class<?> httpClientInterfaceClass) {
    MixHttpClient mixHttpClient = httpClientInterfaceClass.getAnnotation(MixHttpClient.class);
    String poolName = mixHttpClient.poolName();
    Map<String, ConnectionPool> poolRegistry = httpClientConfigBean.getPools();

    Assert.notNull(poolRegistry, "pool cache bean is not exist .");

    ConnectionPool connectionPool = poolRegistry.get(poolName);
    Assert.notNull(connectionPool, "The connection pool corresponding to the current pool name does not exist , pool name = " + poolName);

    return connectionPool;
  }

  /**
   * Get OkHttpClient instance, one interface corresponds to one OkHttpClient
   *
   * @param httpClientInterfaceClass httpClientInterfaceClass
   * @return OkHttpClient instance
   */
  @SuppressWarnings("SwitchStatementWithTooFewBranches")
  private synchronized OkHttpClient getOkHttpClient(Class<?> httpClientInterfaceClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

    MixHttpClient mixHttpClient = httpClientInterfaceClass.getAnnotation(MixHttpClient.class);

    Method method = findOkHttpClientBuilderMethod(httpClientInterfaceClass);

    OkHttpClient.Builder okHttpClientBuilder;

    if (method != null) {
      okHttpClientBuilder = (OkHttpClient.Builder) method.invoke(null);
    } else {

      okhttp3.ConnectionPool connectionPool = getConnectionPool(httpClientInterfaceClass);
      // Construct an OkHttpClient object
      okHttpClientBuilder =
          new OkHttpClient.Builder()
              .connectTimeout(mixHttpClient.connectTimeoutMs(), TimeUnit.MILLISECONDS)
              .readTimeout(mixHttpClient.readTimeoutMs(), TimeUnit.MILLISECONDS)
              .writeTimeout(mixHttpClient.writeTimeoutMs(), TimeUnit.MILLISECONDS)
              .callTimeout(mixHttpClient.callTimeoutMs(), TimeUnit.MILLISECONDS)
              .retryOnConnectionFailure(mixHttpClient.retryOnConnectionFailure())
              .followRedirects(mixHttpClient.followRedirects())
              .followSslRedirects(mixHttpClient.followSslRedirects())
              .pingInterval(mixHttpClient.pingIntervalMs(), TimeUnit.MILLISECONDS)
              .connectionPool(connectionPool);
    }

    // add DegradeInterceptor
    if (httpClientProperties.isEnableDegrade()) {
      DegradeType degradeType = httpClientProperties.getDegradeType();
      switch (degradeType) {
        case SENTINEL: {
          try {
            Class.forName("com.alibaba.csp.sentinel.SphU");
            SentinelDegradeInterceptor sentinelDegradeInterceptor = new SentinelDegradeInterceptor();
            sentinelDegradeInterceptor.setEnvironment(environment);
            sentinelDegradeInterceptor.setResourceNameParser(httpClientConfigBean.getResourceNameParser());
            okHttpClientBuilder.addInterceptor(sentinelDegradeInterceptor);
          } catch (ClassNotFoundException e) {
            log.warn("Class <com.alibaba.csp.sentinel> is not found .");
          }
          break;
        }
        default: {
          throw new IllegalArgumentException("config-ed degrade type is not supported , type = " + degradeType);
        }
      }
    }

    // add ServiceInstanceChooserInterceptor
    if (StringUtils.hasText(mixHttpClient.serviceId())) {
      ServiceInstanceChooserInterceptor serviceInstanceChooserInterceptor = httpClientConfigBean.getServiceInstanceChooserInterceptor();
      if (serviceInstanceChooserInterceptor != null) {
        okHttpClientBuilder.addInterceptor(serviceInstanceChooserInterceptor);
      }
    }

    // add ErrorDecoderInterceptor
    Class<? extends MixHttpClientErrorDecoder> errorDecoderClass = mixHttpClient.errorDecoder();
    MixHttpClientErrorDecoder decoder = getBean(errorDecoderClass);
    if (decoder == null) {
      decoder = errorDecoderClass.newInstance();
    }

    ErrorDecoderInterceptor decoderInterceptor = ErrorDecoderInterceptor.create(decoder);
    okHttpClientBuilder.addInterceptor(decoderInterceptor);

    // Add the interceptor defined by the annotation on the interface
    List<Interceptor> interceptors = new ArrayList<>(findInterceptorByAnnotation(httpClientInterfaceClass));

    // add global interceptor
    Collection<AbstractGlobalInterceptor> globalInterceptors = httpClientConfigBean.getGlobalInterceptors();

    if (!CollectionUtils.isEmpty(globalInterceptors)) {
      interceptors.addAll(globalInterceptors);
    }

    interceptors.forEach(okHttpClientBuilder::addInterceptor);

    // add retry interceptor
    Interceptor retryInterceptor = httpClientConfigBean.getRetryInterceptor();
    okHttpClientBuilder.addInterceptor(retryInterceptor);

    // add log printing interceptor
    if (httpClientProperties.isEnableLog() && mixHttpClient.enableLog()) {

      Class<? extends AbstractLoggingInterceptor> loggingInterceptorClass = httpClientProperties.getLoggingInterceptor();
      Constructor<? extends AbstractLoggingInterceptor> constructor = loggingInterceptorClass.getConstructor(Level.class, MixHttpClientLogStrategy.class);
      AbstractLoggingInterceptor loggingInterceptor = constructor.newInstance(mixHttpClient.logLevel(), mixHttpClient.logStrategy());

      okHttpClientBuilder.addNetworkInterceptor(loggingInterceptor);
    }

    Collection<MixHttpClientInterceptor> networkInterceptors = httpClientConfigBean.getNetworkInterceptors();

    if (!CollectionUtils.isEmpty(networkInterceptors)) {
      for (MixHttpClientInterceptor networkInterceptor : networkInterceptors) {
        okHttpClientBuilder.addNetworkInterceptor(networkInterceptor);
      }
    }

    // build client
    return okHttpClientBuilder.build();
  }

  private <U> U getBean(Class<U> clz) {
    try {
      return applicationContext.getBean(clz);
    } catch (BeansException e) {
      return null;
    }
  }

  private Method findOkHttpClientBuilderMethod(Class<?> httpClientInterfaceClass) {
    Method[] methods = httpClientInterfaceClass.getMethods();
    for (Method method : methods) {
      if (Modifier.isStatic(method.getModifiers())
          && method.isAnnotationPresent(CustomOkHttpClient.class)
          && method.getReturnType().equals(OkHttpClient.Builder.class)) {
        return method;
      }
    }
    return null;
  }

  /**
   * Get the interceptor set defined on the httpClient interface class
   *
   * @param httpClientInterfaceClass httpClientInterfaceClass
   * @return the interceptor list
   */
  @SuppressWarnings("unchecked")
  private List<Interceptor> findInterceptorByAnnotation(Class<?> httpClientInterfaceClass) throws InstantiationException, IllegalAccessException {

    Annotation[] classAnnotations = httpClientInterfaceClass.getAnnotations();
    List<Interceptor> interceptors = new ArrayList<>();

    List<Annotation> interceptAnnotations = new ArrayList<>();
    for (Annotation classAnnotation : classAnnotations) {
      Class<? extends Annotation> annotationType = classAnnotation.annotationType();
      if (annotationType.isAnnotationPresent(InterceptorComponent.class)) {
        interceptAnnotations.add(classAnnotation);
      }
    }

    for (Annotation interceptAnnotation : interceptAnnotations) {
      // Get annotation attribute data
      Map<String, Object> annotationAttributes =
          AnnotationUtils.getAnnotationAttributes(interceptAnnotation);
      Object handler = annotationAttributes.get("handler");
      Assert.notNull(handler, "@InterceptorComponent annotations must be configured: Class<? extends BasePathMatchInterceptor> handler()");
      Assert.notNull(annotationAttributes.get("include"), "@InterceptorComponent annotations must be configured: String[] include()");
      Assert.notNull(annotationAttributes.get("exclude"), "@InterceptorComponent annotations must be configured: String[] exclude()");
      Class<? extends AbstractPathMatchInterceptor> interceptorClass = (Class<? extends AbstractPathMatchInterceptor>) handler;

      AbstractPathMatchInterceptor interceptor = getInterceptorInstance(interceptorClass);
      Map<String, Object> annotationResolveAttributes = new HashMap<>(8);

      annotationAttributes.forEach(
          (key, value) -> {
            if (value instanceof String) {
              String newValue = environment.resolvePlaceholders((String) value);
              annotationResolveAttributes.put(key, newValue);
            } else {
              annotationResolveAttributes.put(key, value);
            }
          });

      // Set property value dynamically
      BeanPropertiesKit.populate(interceptor, annotationResolveAttributes);
      interceptors.add(interceptor);
    }

    // return
    return interceptors;
  }

  /**
   * Obtain the path interceptor instance, first from the spring container. If it does not exist in
   * the spring container, the no-argument constructor will instantiate one.
   *
   * @param interceptorClass A subclass of @{@link AbstractPathMatchInterceptor}
   * @return @{@link AbstractPathMatchInterceptor} instance
   */
  private AbstractPathMatchInterceptor getInterceptorInstance(Class<? extends AbstractPathMatchInterceptor> interceptorClass) throws IllegalAccessException, InstantiationException {
    try {
      return applicationContext.getBean(interceptorClass);
    } catch (BeansException e) {
      return interceptorClass.newInstance();
    }
  }

  /**
   * Obtain a Retrofit instance, a httpClient interface corresponds to a Retrofit instance
   *
   * @param httpClientInterfaceClass httpClientInterfaceClass
   * @return Retrofit instance
   */
  private synchronized Retrofit getRetrofit(Class<?> httpClientInterfaceClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    MixHttpClient mixHttpClient = httpClientInterfaceClass.getAnnotation(MixHttpClient.class);

    String baseUrl = mixHttpClient.baseUrl();

    if (StringUtils.hasText(baseUrl)) {
      baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
      if (!baseUrl.endsWith(SUFFIX)) {
        baseUrl += SUFFIX;
      }
    } else {
      String serviceId = mixHttpClient.serviceId();
      String path = mixHttpClient.path();
      if (!path.endsWith(SUFFIX)) {
        path += SUFFIX;
      }
      baseUrl = "http://" + (serviceId + SUFFIX + path).replaceAll("/+", SUFFIX);
      baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
    }

    OkHttpClient client = getOkHttpClient(httpClientInterfaceClass);

    Retrofit.Builder retrofitBuilder =
        new Retrofit.Builder()
            .baseUrl(baseUrl)
            .validateEagerly(mixHttpClient.validateEagerly())
            .client(client);

    // 添加CallAdapter.Factory
    Class<? extends CallAdapter.Factory>[] callAdapterFactoryClasses = mixHttpClient.callAdapterFactories();
    Class<? extends CallAdapter.Factory>[] globalCallAdapterFactoryClasses = httpClientConfigBean.getGlobalCallAdapterFactoryClasses();
    List<CallAdapter.Factory> callAdapterFactories = getCallAdapterFactories(callAdapterFactoryClasses, globalCallAdapterFactoryClasses);

    if (!CollectionUtils.isEmpty(callAdapterFactories)) {
      callAdapterFactories.forEach(retrofitBuilder::addCallAdapterFactory);
    }
    // 添加Converter.Factory
    Class<? extends Converter.Factory>[] converterFactoryClasses = mixHttpClient.converterFactories();
    Class<? extends Converter.Factory>[] globalConverterFactoryClasses = httpClientConfigBean.getGlobalConverterFactoryClasses();

    List<Converter.Factory> converterFactories = getConverterFactories(converterFactoryClasses, globalConverterFactoryClasses);

    if (!CollectionUtils.isEmpty(converterFactories)) {
      converterFactories.forEach(retrofitBuilder::addConverterFactory);
    }

    return retrofitBuilder.build();
  }

  private List<CallAdapter.Factory> getCallAdapterFactories(
      Class<? extends CallAdapter.Factory>[] callAdapterFactoryClasses,
      Class<? extends CallAdapter.Factory>[] globalCallAdapterFactoryClasses)
      throws IllegalAccessException, InstantiationException {
    List<Class<? extends CallAdapter.Factory>> combineCallAdapterFactoryClasses = new ArrayList<>();

    if (callAdapterFactoryClasses != null && callAdapterFactoryClasses.length != 0) {
      combineCallAdapterFactoryClasses.addAll(Arrays.asList(callAdapterFactoryClasses));
    }

    if (globalCallAdapterFactoryClasses != null && globalCallAdapterFactoryClasses.length != 0) {
      combineCallAdapterFactoryClasses.addAll(Arrays.asList(globalCallAdapterFactoryClasses));
    }

    if (combineCallAdapterFactoryClasses.isEmpty()) {
      return Collections.emptyList();
    }

    List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();

    for (Class<? extends CallAdapter.Factory> callAdapterFactoryClass :
        combineCallAdapterFactoryClasses) {
      CallAdapter.Factory callAdapterFactory =
          CALL_ADAPTER_FACTORIES_CACHE.get(callAdapterFactoryClass);
      if (callAdapterFactory == null) {
        callAdapterFactory = getBean(callAdapterFactoryClass);
        if (callAdapterFactory == null) {
          callAdapterFactory = callAdapterFactoryClass.newInstance();
        }
        CALL_ADAPTER_FACTORIES_CACHE.put(callAdapterFactoryClass, callAdapterFactory);
      }
      callAdapterFactories.add(callAdapterFactory);
    }
    return callAdapterFactories;
  }

  private List<Converter.Factory> getConverterFactories(
      Class<? extends Converter.Factory>[] converterFactoryClasses,
      Class<? extends Converter.Factory>[] globalConverterFactoryClasses)
      throws IllegalAccessException, InstantiationException {
    List<Class<? extends Converter.Factory>> combineConverterFactoryClasses = new ArrayList<>();

    if (converterFactoryClasses != null && converterFactoryClasses.length != 0) {
      combineConverterFactoryClasses.addAll(Arrays.asList(converterFactoryClasses));
    }

    if (globalConverterFactoryClasses != null && globalConverterFactoryClasses.length != 0) {
      combineConverterFactoryClasses.addAll(Arrays.asList(globalConverterFactoryClasses));
    }

    if (combineConverterFactoryClasses.isEmpty()) {
      return Collections.emptyList();
    }

    List<Converter.Factory> converterFactories = new ArrayList<>();

    for (Class<? extends Converter.Factory> converterFactoryClass : combineConverterFactoryClasses) {

      Converter.Factory converterFactory = CONVERTER_FACTORIES_CACHE.get(converterFactoryClass);

      if (converterFactory == null) {
        converterFactory = getBean(converterFactoryClass);
        if (converterFactory == null) {
          converterFactory = converterFactoryClass.newInstance();
        }
        CONVERTER_FACTORIES_CACHE.put(converterFactoryClass, converterFactory);
      }
      converterFactories.add(converterFactory);
    }

    return converterFactories;
  }

  @Override
  public void setEnvironment(@NonNull Environment environment) {
    this.environment = environment;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    this.httpClientConfigBean = applicationContext.getBean(MixHttpClientConfigBean.class);
    this.httpClientProperties = httpClientConfigBean.getHttpClientProperties();
  }
}
