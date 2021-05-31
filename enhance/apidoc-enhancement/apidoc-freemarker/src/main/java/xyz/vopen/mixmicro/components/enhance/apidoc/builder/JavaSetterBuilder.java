package xyz.vopen.mixmicro.components.enhance.apidoc.builder;

import xyz.vopen.mixmicro.components.enhance.apidoc.codegenerator.ICodeBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.FieldModel;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class JavaSetterBuilder implements ICodeBuilder {

  private String setterTemplate;
  private final FieldModel entryFieldModel;

  public JavaSetterBuilder(String setterTemplate, FieldModel entryFieldModel) {
    this.setterTemplate = setterTemplate;
    this.entryFieldModel = entryFieldModel;
  }

  @Override
  public String build() {
    setterTemplate =
        setterTemplate.replace("${REMOTE_FIELD_NAME}", entryFieldModel.getRemoteFieldName());
    setterTemplate =
        setterTemplate.replace("${CASE_FIELD_NAME}", entryFieldModel.getCaseFieldName());
    setterTemplate = setterTemplate.replace("${FIELD_NAME}", entryFieldModel.getFieldName());
    setterTemplate = setterTemplate.replace("${FIELD_TYPE}", entryFieldModel.getFieldType());
    return setterTemplate + "\n";
  }
}
