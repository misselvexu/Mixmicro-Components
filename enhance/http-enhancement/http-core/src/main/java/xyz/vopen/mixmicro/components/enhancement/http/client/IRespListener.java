package xyz.vopen.mixmicro.components.enhancement.http.client;

import xyz.vopen.mixmicro.components.enhancement.http.HttpStatus;
import xyz.vopen.mixmicro.components.enhancement.http.HttpVersion;

import java.util.Map;

/**
 * Interface for response received from server
 * <p/>
 * A low level interface, can be used for very large file download
 *
 * @author feng
 */
public interface IRespListener {

  void onBodyReceived(byte[] buf, int length) throws AbortException;

  void onCompleted();

  void onHeadersReceived(Map<String, Object> headers) throws AbortException;

  void onInitialLineReceived(HttpVersion version, HttpStatus status)
      throws AbortException;

  /**
   * protocol error
   */
  void onThrowable(Throwable t);
}
