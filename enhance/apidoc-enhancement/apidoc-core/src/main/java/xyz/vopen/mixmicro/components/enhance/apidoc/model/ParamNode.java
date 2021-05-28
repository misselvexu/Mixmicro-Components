package xyz.vopen.mixmicro.components.enhance.apidoc.model;

/**
 * a param node corresponds to a request parameter
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class ParamNode {

  private String name;
  private String type;
  private Boolean required = Boolean.FALSE;
  private String description;
  private Boolean jsonBody = Boolean.FALSE; // when true ,the json body set to description

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  public Boolean getJsonBody() {
    return jsonBody;
  }

  public void setJsonBody(Boolean jsonBody) {
    this.jsonBody = jsonBody;
  }
}
