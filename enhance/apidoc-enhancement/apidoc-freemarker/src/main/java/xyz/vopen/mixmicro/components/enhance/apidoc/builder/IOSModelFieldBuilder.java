package xyz.vopen.mixmicro.components.enhance.apidoc.builder;

import xyz.vopen.mixmicro.components.enhance.apidoc.codegenerator.ICodeBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.FieldModel;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class IOSModelFieldBuilder implements ICodeBuilder {

  private String modelFieldTemplate;
  private final FieldModel entryFieldModel;

  public IOSModelFieldBuilder(String modelFieldTemplate, FieldModel entryFieldModel) {
    super();
    this.modelFieldTemplate = modelFieldTemplate;
    this.entryFieldModel = entryFieldModel;
  }

  @Override
  public String build() {
    modelFieldTemplate =
        modelFieldTemplate.replace("${FIELD_TYPE}", entryFieldModel.getIFieldType());
    modelFieldTemplate =
        modelFieldTemplate.replace("${FIELD_NAME}", entryFieldModel.getFieldName());
    modelFieldTemplate = modelFieldTemplate.replace("${COMMENT}", entryFieldModel.getComment());
    modelFieldTemplate = modelFieldTemplate.replace("${ASSIGN}", entryFieldModel.getAssign());
    return modelFieldTemplate + "\n";
  }
}
