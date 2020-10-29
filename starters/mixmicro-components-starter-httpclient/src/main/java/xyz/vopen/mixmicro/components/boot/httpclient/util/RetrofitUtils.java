package xyz.vopen.mixmicro.components.boot.httpclient.util;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import xyz.vopen.mixmicro.components.boot.httpclient.exception.ReadResponseBodyException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class RetrofitUtils {

  private static final Charset UTF8 = StandardCharsets.UTF_8;
  public static final String GZIP = "gzip";
  public static final String CONTENT_ENCODING = "Content-Encoding";
  public static final String IDENTITY = "identity";

  private RetrofitUtils() {
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
}
