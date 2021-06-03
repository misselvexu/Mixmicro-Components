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

  public static final String API_DOC_PREFIX = "mixmicro.apidoc.freemarker";

  /** apidoc is enable */
  private Boolean enable;
  /** current doc version */
  private String currentVersion;
  /** absolute dist path */
  private String distPath;
  /** ignore endpoint packages */
  private Set<String> ignorePackages;
  /** project absolute path */
  private String projectPath;

  public Boolean getEnable() {
    return enable;
  }

  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  public String getCurrentVersion() {
    return currentVersion;
  }

  public void setCurrentVersion(String currentVersion) {
    this.currentVersion = currentVersion;
  }

  public String getDistPath() {
    return distPath;
  }

  public void setDistPath(String distPath) {
    this.distPath = distPath;
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
}
