package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import java.io.Serializable;

/**
 * Created by lzw on 2017/8/23.
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class HeaderNode implements Serializable {
  /** header param name */
  private String name;
  /** header param values */
  private String value;

  public HeaderNode() {}

  public HeaderNode(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
