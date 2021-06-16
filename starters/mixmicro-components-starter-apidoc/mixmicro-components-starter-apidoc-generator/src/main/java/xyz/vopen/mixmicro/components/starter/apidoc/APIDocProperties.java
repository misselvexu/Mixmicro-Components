package xyz.vopen.mixmicro.components.starter.apidoc;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

import static xyz.vopen.mixmicro.components.starter.apidoc.APIDocProperties.API_DOC_PREFIX;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/2
 */
@ConfigurationProperties(prefix = API_DOC_PREFIX)
public class APIDocProperties {

  public static final String API_DOC_PREFIX = "mixmicro.apidoc.client";
  /** current doc version */
  private String currentVersion;
  /** ignore endpoint packages */
  private Set<String> ignorePackages;
  /** project absolute path */
  private String projectPath;
  /** apidoc genType,default:false */
  private Boolean genLocal = false;
  /** mongodb connection uri */
  private String mongoUri = "mongodb://apidoc:apidoc@dev-middle.hgj.net:27017/apidoc";
  /** api doc mongodb default database */
  private String apiDatabase = "apidoc";

  public String getCurrentVersion() {
    return currentVersion;
  }

  public void setCurrentVersion(String currentVersion) {
    this.currentVersion = currentVersion;
  }

  public Set<String> getIgnorePackages() {
    return ignorePackages;
  }

  public void setIgnorePackages(Set<String> ignorePackages) {
    this.ignorePackages = ignorePackages;
  }

  public String getProjectPath() {
    return projectPath;
  }

  public void setProjectPath(String projectPath) {
    this.projectPath = projectPath;
  }

  public Boolean getGenLocal() {
    return genLocal;
  }

  public void setGenLocal(Boolean genLocal) {
    this.genLocal = genLocal;
  }

  public String getMongoUri() {
    return mongoUri;
  }

  public void setMongoUri(String mongoUri) {
    this.mongoUri = mongoUri;
  }

  public String getApiDatabase() {
    return apiDatabase;
  }

  public void setApiDatabase(String apiDatabase) {
    this.apiDatabase = apiDatabase;
  }
}
