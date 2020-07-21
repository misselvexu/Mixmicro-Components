package xyz.vopen.mixmicro.components.enhancement.http.client;

import xyz.vopen.mixmicro.components.enhancement.http.HTTPException;

public class AbortException extends HTTPException {

  public AbortException(String msg) {
    super(msg);
  }

  private static final long serialVersionUID = 1L;

}
