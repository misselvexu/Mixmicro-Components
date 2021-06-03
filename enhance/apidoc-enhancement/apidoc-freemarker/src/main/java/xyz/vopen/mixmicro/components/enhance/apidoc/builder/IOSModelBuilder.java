package xyz.vopen.mixmicro.components.enhance.apidoc.builder;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class IOSModelBuilder implements ICodeBuilder {

  private String modelTemplate;
  private final String objectName;
  private final String properties;

  public IOSModelBuilder(String modelTemplate, String objectName, String properties) {
    super();
    this.modelTemplate = modelTemplate;
    this.objectName = objectName;
    this.properties = properties;
  }

  @Override
  public String build() {
    modelTemplate = modelTemplate.replace("${CLASS_NAME}", objectName);
    modelTemplate = modelTemplate.replace("${FIELDS}", properties);
    return modelTemplate;
  }
}
