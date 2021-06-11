package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import xyz.vopen.mixmicro.components.enhance.apidoc.exception.DHttpException;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class DHttpResponse implements Serializable {

  private int code;
  private InputStream stream;
  private Map<String, String> headers = new HashMap<>();

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public InputStream getStream() {
    return stream;
  }

  public void setStream(InputStream stream) {
    this.stream = stream;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public void addHeader(String key, String value) {
    this.headers.put(key, value);
  }

  public String getHeader(String header) {
    return headers.get(header);
  }

  public String streamAsString() {
    try {
      return CommonUtils.streamToString(stream);
    } catch (IOException e) {
      throw new DHttpException(e);
    }
  }
}
