package xyz.vopen.mixmicro.components.enhance.apidoc.builder;

import xyz.vopen.mixmicro.components.enhance.apidoc.codegenerator.ICodeBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.FieldModel;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class JavaFieldBuilder implements ICodeBuilder {

  private String fieldTemplate;
  private FieldModel entryFieldModel;

  public JavaFieldBuilder(String fieldTemplate, FieldModel entryFieldModel) {
    this.fieldTemplate = fieldTemplate;
    this.entryFieldModel = entryFieldModel;
  }

  @Override
  public String build() {
    fieldTemplate = fieldTemplate.replace("${FIELD_TYPE}", entryFieldModel.getFieldType());
    fieldTemplate = fieldTemplate.replace("${FIELD_NAME}", entryFieldModel.getFieldName());
    fieldTemplate = fieldTemplate.replace("${COMMENT}", entryFieldModel.getComment());
    return fieldTemplate + "\n";
  }
}
