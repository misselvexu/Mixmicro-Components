package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import com.alibaba.fastjson.JSON;

/**
 * response node
 *
 * @author tino.tang
 */
public class ResponseNode extends ClassNode {

  private RequestNode requestNode;

  private String stringResult;

  public RequestNode getRequestNode() {
    return requestNode;
  }

  public void setRequestNode(RequestNode requestNode) {
    this.requestNode = requestNode;
  }

  public String getStringResult() {
    return stringResult;
  }

  public void setStringResult(String stringResult) {
    this.stringResult = stringResult;
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
