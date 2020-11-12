package xyz.vopen.mixmicro.components.boot.httpclient.kits;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.MixHttpClient;
import xyz.vopen.mixmicro.components.boot.httpclient.exception.ReadResponseBodyException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import static xyz.vopen.mixmicro.components.boot.httpclient.core.MixHttpClientFactoryBean.SUFFIX;

public final class MixHttpClientKit {

  private static final Charset UTF8 = StandardCharsets.UTF_8;
  public static final String GZIP = "gzip";
  public static final String CONTENT_ENCODING = "Content-Encoding";
  public static final String IDENTITY = "identity";

  private MixHttpClientKit() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * read ResponseBody as String
   *
   * @param response response
   * @return ResponseBody String
   * @throws ReadResponseBodyException maybe thrown {@link ReadResponseBodyException}
   */
  public static String readResponseBody(Response response) throws ReadResponseBodyException {
    try {
      Headers headers = response.headers();
      if (bodyHasUnknownEncoding(headers)) {
        return null;
      }
      ResponseBody responseBody = response.body();
      if (responseBody == null) {
        return null;
      }
      long contentLength = responseBody.contentLength();

      BufferedSource source = responseBody.source();
      // Buffer the entire body.
      source.request(Long.MAX_VALUE);
      Buffer buffer = source.getBuffer();

      if (GZIP.equalsIgnoreCase(headers.get(CONTENT_ENCODING))) {
        try (GzipSource gzippedResponseBody = new GzipSource(buffer.clone())) {
          buffer = new Buffer();
          buffer.writeAll(gzippedResponseBody);
        }
      }
      Charset charset = UTF8;
      MediaType contentType = responseBody.contentType();
      if (contentType != null) {
        charset = contentType.charset(UTF8);
      }

      if (contentLength != 0) {
        return buffer.clone().readString(charset);
      } else {
        return null;
      }
    } catch (Exception e) {
      throw new ReadResponseBodyException(e);
    }
  }

  private static boolean bodyHasUnknownEncoding(Headers headers) {
    String contentEncoding = headers.get(CONTENT_ENCODING);
    return contentEncoding != null
        && !IDENTITY.equalsIgnoreCase(contentEncoding)
        && !GZIP.equalsIgnoreCase(contentEncoding);
  }

  public static String convertBaseUrl(
      MixHttpClient client, String baseUrl, Environment environment) {
    if (StringUtils.hasText(baseUrl)) {
      baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
      // 解析baseUrl占位符
      if (!baseUrl.endsWith(SUFFIX)) {
        baseUrl += SUFFIX;
      }
    } else {
      String serviceId = client.serviceId();
      String path = client.path();
      if (!path.endsWith(SUFFIX)) {
        path += SUFFIX;
      }
      baseUrl = "http://" + (serviceId + SUFFIX + path).replaceAll("/+", SUFFIX);
      baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
    }
    return baseUrl;
  }

  public static <U> U getBean(ApplicationContext context, Class<U> clz) {
    try {
      U bean = context.getBean(clz);
      return bean;
    } catch (BeansException e) {
      return null;
    }
  }

  public static <U> Collection<U> getBeans(ApplicationContext context, Class<U> clz) {
    try {
      Map<String, U> beanMap = context.getBeansOfType(clz);
      return beanMap.values();
    } catch (BeansException e) {
      // do nothing
    }
    return null;
  }
}
