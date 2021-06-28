package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/**
 * a field node corresponds to a response field
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class FieldNode implements Serializable {
  /** field name */
  private String name;
  /** field type */
  private String type;
  /** field description */
  private String description;
  /** field mock node */
  private MockNode mockNode;
  /** field child node */
  private FieldClassNode childNode;
  /** java generic node */
  private FieldClassNode genericNode;
  /** is array */
  private Boolean isArray;
  /** field position class */
  @Transient private FieldClassNode classNode;
  /** has loop class reference,default false */
  private Boolean loopNode = Boolean.FALSE;
  /** not null,default false */
  private Boolean required = Boolean.FALSE;

  public Boolean getLoopNode() {
    return loopNode;
  }

  public void setLoopNode(Boolean loopNode) {
    this.loopNode = loopNode;
  }

  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public MockNode getMockNode() {
    return mockNode;
  }

  public void setMockNode(MockNode mockNode) {
    this.mockNode = mockNode;
  }

  public FieldClassNode getChildNode() {
    return childNode;
  }

  public void setChildNode(FieldClassNode childNode) {
    this.childNode = childNode;
  }

  public FieldClassNode getGenericNode() {
    return genericNode;
  }

  public void setGenericNode(FieldClassNode genericNode) {
    this.genericNode = genericNode;
  }

  public Boolean getIsArray() {
    return isArray;
  }

  public void setIsArray(Boolean array) {
    isArray = array;
  }

  public FieldClassNode getClassNode() {
    return classNode;
  }

  public void setClassNode(FieldClassNode classNode) {
    this.classNode = classNode;
  }
}
