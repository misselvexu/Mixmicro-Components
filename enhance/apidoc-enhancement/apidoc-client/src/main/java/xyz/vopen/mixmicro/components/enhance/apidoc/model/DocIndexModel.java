package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/2
 */
@Document(collection = "api_doc_index")
public class DocIndexModel implements Serializable {

  /** primary key */
  private String id;
  /** current api version */
  private String currentApiVersion;
  /** project name */
  private String projectName;
  /** record create time */
  private Long createTime;
  /** update time */
  private Long updateTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCurrentApiVersion() {
    return currentApiVersion;
  }

  public void setCurrentApiVersion(String currentApiVersion) {
    this.currentApiVersion = currentApiVersion;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }
}
