package xyz.vopen.mixmicro.components.boot.httpclient.annotation;

import org.slf4j.event.Level;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import xyz.vopen.mixmicro.components.boot.httpclient.core.DefaultHttpClientErrorDecoder;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientErrorDecoder;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientLogStrategy;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MixHttpClient {

  /**
   * An absolute URL (the protocol is necessary). Can be specified as property key, eg:
   * ${propertyKey}. If baseUrl is not configured, you must configure serviceId and path optional
   * configuration.
   *
   * @return baseUrl
   */
  String baseUrl() default "";

  /** The name of the service. Can be specified as property key, eg: ${propertyKey}. */
  String serviceId() default "";

  /** Path prefix to be used by all method-level mappings. */
  String path() default "";

  /**
   * Converter factory for the current interface, higher priority than global converter factory. The
   * converter instance is first obtained from the Spring container. If it is not obtained, it is
   * created by reflection.
   */
  Class<? extends Converter.Factory>[] converterFactories() default {};

  /**
   * Adapter factory for the current interface, higher priority than global callAdapter factory. The
   * converter instance is first obtained from the Spring container. If it is not obtained, it is
   * created by reflection.
   */
  Class<? extends CallAdapter.Factory>[] callAdapterFactories() default {};

  /**
   * When calling {@link Retrofit#create(Class)} on the resulting {@link Retrofit} instance, eagerly
   * validate the configuration of all methods in the supplied interface.
   *
   * @return validateEagerly
   */
  boolean validateEagerly() default false;

  /**
   * The error decoder used in the current interface will decode HTTP related information into the
   * exception when an exception occurs in the request or an invalid response result is received.
   * The invalid response is determined by the business itself. In general, the invalid response
   * corresponding to each service is different, you can customize the corresponding {@link
   * MixHttpClientErrorDecoder}, and then configure it here.
   *
   * @return ErrorDecoder
   */
  Class<? extends MixHttpClientErrorDecoder> errorDecoder() default DefaultHttpClientErrorDecoder.class;

  /**
   * connection pool name
   *
   * @return connection pool name
   */
  String poolName() default "default";

  /**
   * Sets the default connect timeout for new connections. A value of 0 means no timeout, otherwise
   * values must be between 1 and Integer.MAX_VALUE when converted to milliseconds.
   *
   * @return connectTimeoutMs
   */
  int connectTimeoutMs() default 10_000;

  /**
   * Sets the default read timeout for new connections. A value of 0 means no timeout, otherwise
   * values must be between 1 and Integer.MAX_VALUE when converted to milliseconds.
   *
   * @return readTimeoutMs
   */
  int readTimeoutMs() default 10_000;

  /**
   * Sets the default write timeout for new connections. A value of 0 means no timeout, otherwise
   * values must be between 1 and Integer.MAX_VALUE when converted to milliseconds
   *
   * @return writeTimeoutMs
   */
  int writeTimeoutMs() default 10_000;

  /**
   * Sets the default timeout for complete calls. A value of 0 means no timeout, otherwise values
   * must be between 1 and Integer.MAX_VALUE when converted to milliseconds.
   *
   * @return callTimeout
   */
  int callTimeoutMs() default 0;

  /**
   * Sets the interval between HTTP/2 and web socket pings initiated by this client. Use this to
   * automatically send ping frames until either the connection fails or it is closed. This keeps
   * the connection alive and may detect connectivity failures.
   *
   * @return pingInterval
   */
  int pingIntervalMs() default 0;

  /**
   * Configure this client to allow protocol redirects from HTTPS to HTTP and from HTTP to HTTPS.
   * Redirects are still first restricted by followRedirects. Defaults to true.
   *
   * @return followSslRedirects
   */
  boolean followSslRedirects() default true;

  /**
   * Configure this client to follow redirects. If unset, redirects will be followed.
   *
   * @return followRedirects
   */
  boolean followRedirects() default true;

  /**
   * Configure this client to retry or not when a connectivity problem is encountered. By default,
   * this client silently recovers from the following problems:
   *
   * @return retryOnConnectionFailure
   */
  boolean retryOnConnectionFailure() default true;

  /** Whether to enable log printing for the current interface */
  boolean enableLog() default true;

  /**
   * Log printing level, see {@link Level} for supported log levels
   *
   * @return logLevel
   */
  Level logLevel() default Level.INFO;

  /**
   * Log printing strategy, see {@link MixHttpClientLogStrategy} for
   * supported log printing strategies
   *
   * @return logStrategy
   */
  MixHttpClientLogStrategy logStrategy() default MixHttpClientLogStrategy.BASIC;
}
