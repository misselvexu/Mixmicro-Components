package xyz.vopen.mixmicro.components.enhance.apidoc.exception;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class JavaFileNotFoundException extends RuntimeException {

  public JavaFileNotFoundException() {}

  public JavaFileNotFoundException(String message) {
    super(message);
  }
}
