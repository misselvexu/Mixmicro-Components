package xyz.vopen.mixmicro.components.enhancement.http;

// TODO 431 Request Header Fields Too Large
// http://www.rooftopsolutions.nl/blog/new-http-status-codes

public class LineTooLargeException extends HTTPException {

  private static final long serialVersionUID = 1L;

  public LineTooLargeException(String msg) {
    super(msg);
  }
}
