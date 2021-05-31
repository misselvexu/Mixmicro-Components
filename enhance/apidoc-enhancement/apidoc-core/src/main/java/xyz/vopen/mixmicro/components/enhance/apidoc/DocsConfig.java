package xyz.vopen.mixmicro.components.enhance.apidoc;

import xyz.vopen.mixmicro.components.enhance.apidoc.plugin.RapSupportPlugin;

import java.util.*;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class DocsConfig {

  String projectPath; // must set
  List<String> javaSrcPaths = new ArrayList<>(); // multi modules support
  String docsPath; // default equals projectPath
  String resourcePath; // if empty, use the default resources
  String mvcFramework; // spring, play, jfinal, generic, can be empty
  String apiVersion; // this api version
  String projectName; // project name
  Boolean autoGenerate = Boolean.FALSE; // 自动生成所有Controller的接口文档，不需要@ApiDoc注解
  Locale locale = Locale.getDefault();
  Boolean openReflection = Boolean.TRUE; // 是否开启对象反射

  String rapHost;
  String rapLoginCookie;
  String rapProjectId;
  String rapAccount;
  String rapPassword;
  Set<String> ignorePackages = new HashSet<>();

  List<IPluginSupport> plugins = new ArrayList<>();

  List<IPluginSupport> getPlugins() {
    return plugins;
  }

  /**
   * add your own plugin, example:
   *
   * @see RapSupportPlugin
   * @param plugin 配置
   */
  public void addPlugin(IPluginSupport plugin) {
    this.plugins.add(plugin);
  }

  boolean isSpringMvcProject() {
    return mvcFramework != null && mvcFramework.equals("spring");
  }

  boolean isPlayProject() {
    return mvcFramework != null && mvcFramework.equals("play");
  }

  boolean isJfinalProject() {
    return mvcFramework != null && mvcFramework.equals("jfinal");
  }

  boolean isGeneric() {
    return mvcFramework != null && mvcFramework.equals("generic");
  }

  public void setProjectPath(String projectPath) {
    this.projectPath = projectPath;
  }

  public void setDocsPath(String docsPath) {
    this.docsPath = docsPath;
  }

  public void setMvcFramework(String mvcFramework) {
    this.mvcFramework = mvcFramework;
  }

  public List<String> getJavaSrcPaths() {
    return javaSrcPaths;
  }

  public String getApiVersion() {
    return apiVersion;
  }

  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  public Boolean getAutoGenerate() {
    return autoGenerate;
  }

  public void setAutoGenerate(Boolean autoGenerate) {
    this.autoGenerate = autoGenerate;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public String getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(String resourcePath) {
    this.resourcePath = resourcePath;
  }

  /**
   * if cannot find the java file from other module, you can try to config the java src path
   * yourself.
   *
   * @param javaSrcPath java src path
   */
  public void addJavaSrcPath(String javaSrcPath) {
    javaSrcPaths.add(javaSrcPath);
  }

  public String getRapHost() {
    return rapHost;
  }

  public String getRapLoginCookie() {
    return rapLoginCookie;
  }

  public String getRapProjectId() {
    return rapProjectId;
  }

  public String getRapAccount() {
    return rapAccount;
  }

  public String getRapPassword() {
    return rapPassword;
  }

  public Boolean getOpenReflection() {
    return openReflection;
  }

  public void setOpenReflection(Boolean openReflection) {
    this.openReflection = openReflection;
  }

  public Set<String> getIgnorePackages() {
    return ignorePackages;
  }

  public void setIgnorePackages(Set<String> ignorePackages) {
    this.ignorePackages = ignorePackages;
  }

  public void addIgnorePackages(String ignorePackage) {
    this.ignorePackages.add(ignorePackage);
  }
}
