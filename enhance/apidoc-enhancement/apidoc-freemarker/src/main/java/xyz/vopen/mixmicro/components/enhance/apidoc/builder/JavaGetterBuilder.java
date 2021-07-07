package xyz.vopen.mixmicro.components.enhance.apidoc.builder;

import xyz.vopen.mixmicro.components.enhance.apidoc.model.FieldModel;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class JavaGetterBuilder implements ICodeBuilder {

  private String getterTemplate;
  private final FieldModel entryFieldModel;

  public JavaGetterBuilder(String getterTemplate, FieldModel entryFieldModel) {
    this.getterTemplate = getterTemplate;
    this.entryFieldModel = entryFieldModel;
  }

  @Override
  public String build() {
    getterTemplate =
        getterTemplate.replace("${CASE_FIELD_NAME}", entryFieldModel.getCaseFieldName());
    getterTemplate = getterTemplate.replace("${FIELD_NAME}", entryFieldModel.getFieldName());
    getterTemplate = getterTemplate.replace("${FIELD_TYPE}", entryFieldModel.getFieldType());
    getterTemplate =
        getterTemplate.replace("${REMOTE_FIELD_NAME}", entryFieldModel.getRemoteFieldName());
    return getterTemplate + "\n";
  }
}
