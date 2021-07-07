package xyz.vopen.mixmicro.components.enhance.apidoc.bean;

import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/1
 */
public class IndexBean implements Serializable {

  /** current api version */
  private String currentApiVersion;

  /** api version list */
  private List<String> apiVersionList;

  /** project name */
  private String projectName;

  /** controller list */
  private List<ControllerNode> controllerNodeList;

  public String getCurrentApiVersion() {
    return currentApiVersion;
  }

  public void setCurrentApiVersion(String currentApiVersion) {
    this.currentApiVersion = currentApiVersion;
  }

  public List<String> getApiVersionList() {
    return apiVersionList;
  }

  public void setApiVersionList(List<String> apiVersionList) {
    this.apiVersionList = apiVersionList;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public List<ControllerNode> getControllerNodeList() {
    return controllerNodeList;
  }

  public void setControllerNodeList(List<ControllerNode> controllerNodeList) {
    this.controllerNodeList = controllerNodeList;
  }
}
