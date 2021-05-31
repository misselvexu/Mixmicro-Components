package xyz.vopen.mixmicro.components.enhance.apidoc.plugin;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
class Page {

  private int id;
  private String name;
  private String introduction;
  private Module module;
  private Set<Action> actionList = new HashSet<>();
  private String template;

  public static Page newPage() {
    Page page = new Page();
    page.setId(-1);
    return page;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public Set<Action> getActionList() {
    return actionList;
  }

  public void setActionList(Set<Action> actionList) {
    this.actionList = actionList;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }
}
