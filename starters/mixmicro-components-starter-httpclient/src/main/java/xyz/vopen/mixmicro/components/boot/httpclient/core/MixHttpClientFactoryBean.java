package xyz.vopen.mixmicro.components.boot.httpclient.core;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
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
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientLogStrategy;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.InterceptorComponent;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.MixHttpClient;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.CustomOkHttpClient;
import xyz.vopen.mixmicro.components.boot.httpclient.autoconfigure.MixHttpClientConfigBean;
import xyz.vopen.mixmicro.components.boot.httpclient.autoconfigure.MixHttpClientProperties;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.*;
import xyz.vopen.mixmicro.components.boot.httpclient.kits.BeanPropertiesKit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MixHttpClientFactoryBean<T>
    implements FactoryBean<T>, EnvironmentAware, ApplicationContextAware {

  public static final String SUFFIX = "/";
  private static final Map<Class<? extends CallAdapter.Factory>, CallAdapter.Factory>
      CALL_ADAPTER_FACTORIES_CACHE = new HashMap<>(4);

  private final Class<T> retrofitInterface;

  private Environment environment;

  private MixHttpClientProperties httpClientProperties;

  private MixHttpClientConfigBean httpClientConfigBean;

  private ApplicationContext applicationContext;

  private static final Map<Class<? extends Converter.Factory>, Converter.Factory>
      CONVERTER_FACTORIES_CACHE = new HashMap<>(4);

  public MixHttpClientFactoryBean(Class<T> retrofitInterface) {
    this.retrofitInterface = retrofitInterface;
  }

  @Override
  public T getObject() throws Exception {
    checkRetrofitInterface(retrofitInterface);
    Retrofit retrofit = getRetrofit(retrofitInterface);
    return retrofit.create(retrofitInterface);
  }

  /**
   * RetrofitInterface检查
   *
   * @param retrofitInterface .
   */
  private void checkRetrofitInterface(Class<T> retrofitInterface) {
    // check class type
    Assert.isTrue(
        retrofitInterface.isInterface(),
        "@RetrofitClient can only be marked on the interface type!");
    Method[] methods = retrofitInterface.getMethods();

    MixHttpClient mixHttpClient = retrofitInterface.getAnnotation(MixHttpClient.class);

    Assert.isTrue(
        StringUtils.hasText(mixHttpClient.baseUrl())
            || StringUtils.hasText(mixHttpClient.serviceId()),
        "@RetrofitClient's baseUrl and serviceId must be configured with one！");

    for (Method method : methods) {
      Class<?> returnType = method.getReturnType();
      if (method.isAnnotationPresent(CustomOkHttpClient.class)) {
        Assert.isTrue(
            returnType.equals(OkHttpClient.Builder.class),
            "For methods annotated by @OkHttpClientBuilder, the return value must be OkHttpClient.Builder！");
        Assert.isTrue(
            Modifier.isStatic(method.getModifiers()),
            "only static method can annotated by @OkHttpClientBuilder!");
        continue;
      }

      Assert.isTrue(
          !void.class.isAssignableFrom(returnType),
          "The void keyword is not supported as the return type, please use java.lang.Void！ method="
              + method);
      if (httpClientProperties.isDisableVoidReturnType()) {
        Assert.isTrue(
            !Void.class.isAssignableFrom(returnType),
            "Configured to disable Void as the return value, please specify another return type!method="
                + method);
      }
    }
  }

  @Override
  public Class<T> getObjectType() {
    return this.retrofitInterface;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  /**
   * Get okhttp3 connection pool
   *
   * @param retrofitClientInterfaceClass retrofitClientInterfaceClass
   * @return okhttp3 connection pool
   */
  private synchronized okhttp3.ConnectionPool getConnectionPool(
      Class<?> retrofitClientInterfaceClass) {
    MixHttpClient mixHttpClient = retrofitClientInterfaceClass.getAnnotation(MixHttpClient.class);
    String poolName = mixHttpClient.poolName();
    Map<String, ConnectionPool> poolRegistry = httpClientConfigBean.getPoolRegistry();
    Assert.notNull(
        poolRegistry, "poolRegistry does not exist! Please set retrofitConfigBean.poolRegistry!");
    ConnectionPool connectionPool = poolRegistry.get(poolName);
    Assert.notNull(
        connectionPool,
        "The connection pool corresponding to the current poolName does not exist! poolName = "
            + poolName);
    return connectionPool;
  }

  /**
   * Get OkHttpClient instance, one interface corresponds to one OkHttpClient
   *
   * @param retrofitClientInterfaceClass retrofitClientInterfaceClass
   * @return OkHttpClient instance
   */
  private synchronized OkHttpClient getOkHttpClient(Class<?> retrofitClientInterfaceClass)
      throws IllegalAccessException, InstantiationException, NoSuchMethodException,
          InvocationTargetException {
    MixHttpClient mixHttpClient = retrofitClientInterfaceClass.getAnnotation(MixHttpClient.class);
    Method method = findOkHttpClientBuilderMethod(retrofitClientInterfaceClass);
    OkHttpClient.Builder okHttpClientBuilder;
    if (method != null) {
      okHttpClientBuilder = (OkHttpClient.Builder) method.invoke(null);
    } else {
      okhttp3.ConnectionPool connectionPool = getConnectionPool(retrofitClientInterfaceClass);
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

    // add ServiceInstanceChooserInterceptor
    if (StringUtils.hasText(mixHttpClient.serviceId())) {
      ServiceInstanceChooserInterceptor serviceInstanceChooserInterceptor =
          httpClientConfigBean.getServiceInstanceChooserInterceptor();
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
    List<Interceptor> interceptors =
        new ArrayList<>(findInterceptorByAnnotation(retrofitClientInterfaceClass));
    // add global interceptor
    Collection<AbstractGlobalInterceptor> globalInterceptors =
        httpClientConfigBean.getGlobalInterceptors();
    if (!CollectionUtils.isEmpty(globalInterceptors)) {
      interceptors.addAll(globalInterceptors);
    }
    interceptors.forEach(okHttpClientBuilder::addInterceptor);

    // add retry interceptor
    Interceptor retryInterceptor = httpClientConfigBean.getRetryInterceptor();
    okHttpClientBuilder.addInterceptor(retryInterceptor);

    // add log printing interceptor
    if (httpClientProperties.isEnableLog() && mixHttpClient.enableLog()) {
      Class<? extends AbstractLoggingInterceptor> loggingInterceptorClass =
          httpClientProperties.getLoggingInterceptor();
      Constructor<? extends AbstractLoggingInterceptor> constructor =
          loggingInterceptorClass.getConstructor(Level.class, MixHttpClientLogStrategy.class);
      AbstractLoggingInterceptor loggingInterceptor =
          constructor.newInstance(mixHttpClient.logLevel(), mixHttpClient.logStrategy());
      okHttpClientBuilder.addNetworkInterceptor(loggingInterceptor);
    }

    Collection<NetworkInterceptor> networkInterceptors =
        httpClientConfigBean.getNetworkInterceptors();
    if (!CollectionUtils.isEmpty(networkInterceptors)) {
      for (NetworkInterceptor networkInterceptor : networkInterceptors) {
        okHttpClientBuilder.addNetworkInterceptor(networkInterceptor);
      }
    }

    return okHttpClientBuilder.build();
  }

  private <U> U getBean(Class<U> clz) {
    try {
      U bean = applicationContext.getBean(clz);
      return bean;
    } catch (BeansException e) {
      return null;
    }
  }

  private Method findOkHttpClientBuilderMethod(Class<?> retrofitClientInterfaceClass) {
    Method[] methods = retrofitClientInterfaceClass.getMethods();
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
   * Get the interceptor set defined on the retrofitClient interface class
   *
   * @param retrofitClientInterfaceClass retrofitClientInterfaceClass
   * @return the interceptor list
   */
  @SuppressWarnings("unchecked")
  private List<Interceptor> findInterceptorByAnnotation(Class<?> retrofitClientInterfaceClass)
      throws InstantiationException, IllegalAccessException {
    Annotation[] classAnnotations = retrofitClientInterfaceClass.getAnnotations();
    List<Interceptor> interceptors = new ArrayList<>();
    // 找出被@InterceptMark标记的注解。Find the annotation marked by @InterceptMark
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
      Assert.notNull(
          handler,
          "@InterceptMark annotations must be configured: Class<? extends BasePathMatchInterceptor> handler()");
      Assert.notNull(
          annotationAttributes.get("include"),
          "@InterceptMark annotations must be configured: String[] include()");
      Assert.notNull(
          annotationAttributes.get("exclude"),
          "@InterceptMark annotations must be configured: String[] exclude()");
      Class<? extends AbstractPathMatchInterceptor> interceptorClass =
          (Class<? extends AbstractPathMatchInterceptor>) handler;
      AbstractPathMatchInterceptor interceptor = getInterceptorInstance(interceptorClass);
      Map<String, Object> annotationResolveAttributes = new HashMap<>(8);
      // 占位符属性替换。Placeholder attribute replacement
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
    return interceptors;
  }

  /**
   * Obtain the path interceptor instance, first from the spring container. If it does not exist in
   * the spring container, the no-argument constructor will instantiate one.
   *
   * @param interceptorClass A subclass of @{@link AbstractPathMatchInterceptor}
   * @return @{@link AbstractPathMatchInterceptor} instance
   */
  private AbstractPathMatchInterceptor getInterceptorInstance(
      Class<? extends AbstractPathMatchInterceptor> interceptorClass)
      throws IllegalAccessException, InstantiationException {
    try {
      return applicationContext.getBean(interceptorClass);
    } catch (BeansException e) {
      return interceptorClass.newInstance();
    }
  }

  /**
   * Obtain a Retrofit instance, a retrofitClient interface corresponds to a Retrofit instance
   *
   * @param retrofitClientInterfaceClass retrofitClientInterfaceClass
   * @return Retrofit instance
   */
  private synchronized Retrofit getRetrofit(Class<?> retrofitClientInterfaceClass)
      throws InstantiationException, IllegalAccessException, NoSuchMethodException,
          InvocationTargetException {
    MixHttpClient mixHttpClient = retrofitClientInterfaceClass.getAnnotation(MixHttpClient.class);
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

    OkHttpClient client = getOkHttpClient(retrofitClientInterfaceClass);
    Retrofit.Builder retrofitBuilder =
        new Retrofit.Builder()
            .baseUrl(baseUrl)
            .validateEagerly(mixHttpClient.validateEagerly())
            .client(client);

    // 添加CallAdapter.Factory
    Class<? extends CallAdapter.Factory>[] callAdapterFactoryClasses =
        mixHttpClient.callAdapterFactories();
    Class<? extends CallAdapter.Factory>[] globalCallAdapterFactoryClasses =
        httpClientConfigBean.getGlobalCallAdapterFactoryClasses();
    List<CallAdapter.Factory> callAdapterFactories =
        getCallAdapterFactories(callAdapterFactoryClasses, globalCallAdapterFactoryClasses);
    if (!CollectionUtils.isEmpty(callAdapterFactories)) {
      callAdapterFactories.forEach(retrofitBuilder::addCallAdapterFactory);
    }
    // 添加Converter.Factory
    Class<? extends Converter.Factory>[] converterFactoryClasses =
        mixHttpClient.converterFactories();
    Class<? extends Converter.Factory>[] globalConverterFactoryClasses =
        httpClientConfigBean.getGlobalConverterFactoryClasses();

    List<Converter.Factory> converterFactories =
        getConverterFactories(converterFactoryClasses, globalConverterFactoryClasses);
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

    for (Class<? extends Converter.Factory> converterFactoryClass :
        combineConverterFactoryClasses) {
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
    this.httpClientProperties = httpClientConfigBean.getRetrofitProperties();
  }
}
