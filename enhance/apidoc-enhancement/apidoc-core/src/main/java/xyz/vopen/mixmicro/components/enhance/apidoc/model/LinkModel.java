package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class LinkModel implements Serializable {
  /** link name */
  private String name;
  /** link url */
  private String url;

  public LinkModel(String name, String url) {
    this.name = name;
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
