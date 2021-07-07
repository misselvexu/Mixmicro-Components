package xyz.vopen.mixmicro.components.enhance.apidoc.exception;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/5/31
 */
public class PluginException extends RuntimeException {

  public PluginException() {}

  public PluginException(String message) {
    super(message);
  }

  public PluginException(Throwable cause) {
    super(cause);
  }

  public PluginException(String message, Throwable cause) {
    super(message, cause);
  }
}
