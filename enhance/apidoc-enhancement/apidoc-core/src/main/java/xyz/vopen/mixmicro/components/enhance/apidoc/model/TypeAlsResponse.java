package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import java.io.Serializable;

/**
 * generic type analyse response bean
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/11
 */
public class TypeAlsResponse implements Serializable {
  /** class type */
  private String type;
  /** is array type */
  private Boolean isArray = false;
  /** type class generic node */
  private ClassNode genericClassNode;
  /** field generic node */
  private FieldClassNode genericFieldClassNode;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Boolean getIsArray() {
    return isArray;
  }

  public void setIsArray(Boolean array) {
    isArray = array;
  }

  public ClassNode getGenericClassNode() {
    return genericClassNode;
  }

  public void setGenericClassNode(ClassNode genericClassNode) {
    this.genericClassNode = genericClassNode;
  }

  public FieldClassNode getGenericFieldClassNode() {
    return genericFieldClassNode;
  }

  public void setGenericFieldClassNode(FieldClassNode genericFieldClassNode) {
    this.genericFieldClassNode = genericFieldClassNode;
  }
}
