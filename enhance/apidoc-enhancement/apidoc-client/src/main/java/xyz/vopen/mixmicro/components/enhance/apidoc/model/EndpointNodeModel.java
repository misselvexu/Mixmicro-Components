package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/2
 */
@Document(collection = "api_doc_endpoint_node")
public class EndpointNodeModel implements Serializable {
  /** primary key */
  private String id;
  /** index id */
  private String indexId;
  /** class author */
  private String author;
  /** endpoint description */
  private String description;
  /** request mapping url */
  private String baseUrl;
  /** endpoint class name */
  private String className;
  /** java package name */
  private String packageName;
  /** request method node data */
  private List<RequestNode> requestNodes = new ArrayList<>();
  /** source file name */
  private String srcFileName;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIndexId() {
    return indexId;
  }

  public void setIndexId(String indexId) {
    this.indexId = indexId;
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
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public List<RequestNode> getRequestNodes() {
    return requestNodes;
  }

  public void setRequestNodes(List<RequestNode> requestNodes) {
    this.requestNodes = requestNodes;
  }

  public String getSrcFileName() {
    return srcFileName;
  }

  public void setSrcFileName(String srcFileName) {
    this.srcFileName = srcFileName;
  }
}
