package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import com.alibaba.fastjson.JSON;

/**
 * response node
 *
 * @author tino.tang
 */
public class ResponseNode extends ClassNode {
  /** method name */
  private String methodName;
  /** controller class name */
  private String controllerClassName;
  /** controller class name */
  private String controllerPackageName;
  /** string result */
  private String stringResult;
  /** request url */
  private String requestUrl;

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getControllerClassName() {
    return controllerClassName;
  }

  public void setControllerClassName(String controllerClassName) {
    this.controllerClassName = controllerClassName;
  }

  public String getControllerPackageName() {
    return controllerPackageName;
  }

  public void setControllerPackageName(String controllerPackageName) {
    this.controllerPackageName = controllerPackageName;
  }

  public String getStringResult() {
    return stringResult;
  }

  public void setStringResult(String stringResult) {
    this.stringResult = stringResult;
  }

  public String getRequestUrl() {
    return requestUrl;
  }

  public void setRequestUrl(String requestUrl) {
    this.requestUrl = requestUrl;
  }

  @Override
  public String toJsonApi() {
    if (stringResult != null) {
      try {
        return JSON.toJSONString(JSON.parse(stringResult), true);
      } catch (Exception ex) {
        // do nothing
        return stringResult;
      }
    }
    return super.toJsonApi();
  }
}
