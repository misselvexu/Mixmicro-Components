package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * a controller node corresponds to a controller file
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class ControllerNode implements Serializable {
  /** controller author */
  private String author;
  /** controller description */
  private String description;
  /** controller base url */
  private String baseUrl;
  /** controller class name */
  private String className;
  /** controller package name */
  private String packageName;
  /** generate docs,default false */
  private Boolean generateDocs = Boolean.FALSE;
  /** request nodes */
  @Transient private List<RequestNode> requestNodes = new ArrayList<>();
  /** source file name */
  private String srcFileName;
  /** api doc file name */
  private String docFileName;

  public String getPackageName() {
    return packageName;
  }

  public String getSrcFileName() {
    return srcFileName;
  }

  public void setSrcFileName(String srcFileName) {
    this.srcFileName = srcFileName;
  }

  public Boolean getGenerateDocs() {
    return generateDocs;
  }

  public void setGenerateDocs(Boolean generateDocs) {
    this.generateDocs = generateDocs;
  }

  public String getDocFileName() {
    return docFileName;
  }

  public void setDocFileName(String docFileName) {
    this.docFileName = docFileName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getBaseUrl() {
    return baseUrl == null ? "" : baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public List<RequestNode> getRequestNodes() {
    return requestNodes;
  }

  public void setRequestNodes(List<RequestNode> requestNodes) {
    this.requestNodes = requestNodes;
  }

  public void addRequestNode(RequestNode requestNode) {
    requestNodes.add(requestNode);
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }
}
