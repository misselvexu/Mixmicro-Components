package xyz.vopen.mixmicro.components.boot.httpclient.autoconfigure;

import okhttp3.ConnectionPool;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.degrade.AbstractResourceNameParser;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.AbstractGlobalInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.ServiceInstanceChooserInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.retry.AbstractRetryInterceptor;

import java.util.Collection;
import java.util.Map;

public class MixHttpClientConfigBean {

  private final MixHttpClientProperties httpClientProperties;

  private Map<String, ConnectionPool> pools;

  private Collection<AbstractGlobalInterceptor> globalInterceptors;

  private Collection<MixHttpClientInterceptor> networkInterceptors;

  private AbstractRetryInterceptor retryInterceptor;

  private ServiceInstanceChooserInterceptor serviceInstanceChooserInterceptor;

  private Class<? extends Converter.Factory>[] globalConverterFactoryClasses;

  private Class<? extends CallAdapter.Factory>[] globalCallAdapterFactoryClasses;

  private AbstractResourceNameParser resourceNameParser;

  public MixHttpClientProperties getHttpClientProperties() {
    return httpClientProperties;
  }

  public MixHttpClientConfigBean(MixHttpClientProperties httpClientProperties) {
    this.httpClientProperties = httpClientProperties;
  }

  public Map<String, ConnectionPool> getPools() {
    return pools;
  }

  public void setPools(Map<String, ConnectionPool> pools) {
    this.pools = pools;
  }

  public Collection<AbstractGlobalInterceptor> getGlobalInterceptors() {
    return globalInterceptors;
  }

  public void setGlobalInterceptors(Collection<AbstractGlobalInterceptor> globalInterceptors) {
    this.globalInterceptors = globalInterceptors;
  }

  public AbstractRetryInterceptor getRetryInterceptor() {
    return retryInterceptor;
  }

  public void setRetryInterceptor(AbstractRetryInterceptor retryInterceptor) {
    this.retryInterceptor = retryInterceptor;
  }

  public Collection<MixHttpClientInterceptor> getNetworkInterceptors() {
    return networkInterceptors;
  }

  public void setNetworkInterceptors(Collection<MixHttpClientInterceptor> networkInterceptors) {
    this.networkInterceptors = networkInterceptors;
  }

  public ServiceInstanceChooserInterceptor getServiceInstanceChooserInterceptor() {
    return serviceInstanceChooserInterceptor;
  }

  public AbstractResourceNameParser getResourceNameParser() {
    return resourceNameParser;
  }

  public void setResourceNameParser(AbstractResourceNameParser resourceNameParser) {
    this.resourceNameParser = resourceNameParser;
  }

  public void setServiceInstanceChooserInterceptor(
      ServiceInstanceChooserInterceptor serviceInstanceChooserInterceptor) {
    this.serviceInstanceChooserInterceptor = serviceInstanceChooserInterceptor;
  }

  public Class<? extends Converter.Factory>[] getGlobalConverterFactoryClasses() {
    return globalConverterFactoryClasses;
  }

  public void setGlobalConverterFactoryClasses(
      Class<? extends Converter.Factory>[] globalConverterFactoryClasses) {
    this.globalConverterFactoryClasses = globalConverterFactoryClasses;
  }

  public Class<? extends CallAdapter.Factory>[] getGlobalCallAdapterFactoryClasses() {
    return globalCallAdapterFactoryClasses;
  }

  public void setGlobalCallAdapterFactoryClasses(
      Class<? extends CallAdapter.Factory>[] globalCallAdapterFactoryClasses) {
    this.globalCallAdapterFactoryClasses = globalCallAdapterFactoryClasses;
  }
}
