package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/7
 */
public class  FieldChildNode implements Serializable {
  /** field name */
  private String name;
  /** field type */
  private String type;
  /** field description */
  private String description;
  /** field mock node */
  private MockNode mockNode;
  /** has loop class reference,default false */
  private Boolean loopNode = Boolean.FALSE;
  /** not null,default false */
  private Boolean notNull = Boolean.FALSE;

  public Boolean getLoopNode() {
    return loopNode;
  }

  public void setLoopNode(Boolean loopNode) {
    this.loopNode = loopNode;
  }

  public Boolean getNotNull() {
    return notNull;
  }

  public void setNotNull(Boolean notNull) {
    this.notNull = notNull;
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
}
