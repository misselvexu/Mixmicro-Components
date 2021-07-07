package xyz.vopen.mixmicro.components.enhance.apidoc.plugin;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
class Module {

  public static final String MODULE_NAME = "API List";

  private int id;
  private int projectId;
  private String name;
  private String introduction;
  private Project project;
  private Set<Page> pageList = new HashSet<>();

  public static Module newModule() {
    Module module = new Module();
    module.setId(-1);
    module.setName(MODULE_NAME);
    return module;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public Set<Page> getPageList() {
    return pageList;
  }

  public void setPageList(Set<Page> pageList) {
    this.pageList = pageList;
  }
}
