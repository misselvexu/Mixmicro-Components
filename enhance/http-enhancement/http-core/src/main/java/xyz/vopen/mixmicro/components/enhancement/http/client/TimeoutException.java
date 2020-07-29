package xyz.vopen.mixmicro.components.enhancement.http.client;

import xyz.vopen.mixmicro.components.enhancement.http.HTTPException;

public class TimeoutException extends HTTPException {

  private static final long serialVersionUID = 1L;

  public TimeoutException(String msg) {
    super(msg);
  }
}
