package xyz.vopen.mixmicro.components.enhance.apidoc.consts;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public enum ActionType {
  GET("1"),
  POST("2"),
  PUT("3"),
  DELETE("4");

  public final String type;

  ActionType(String type) {
    this.type = type;
  }
}
