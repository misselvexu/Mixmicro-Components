package xyz.vopen.mixmicro.components.enhance.apidoc.plugin;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
class ModelResponse {

  private int code;
  private String msg;
  private Project model;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Project getModel() {
    return model;
  }

  public void setModel(Project model) {
    this.model = model;
  }
}
