package xyz.vopen.mixmicro.components.enhancement.http.logger;

import xyz.vopen.mixmicro.components.enhancement.http.HttpUtils;

public interface ContextLogger<X, Y> {

  public void log(X event, Y context);

  public ContextLogger<String, Throwable> ERROR_PRINTER = new ContextLogger<String, Throwable>() {
    @Override
    public void log(String event, Throwable e) {
      HttpUtils.printError(event, e);
    }
  };

}
