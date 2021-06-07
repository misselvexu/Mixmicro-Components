package xyz.vopen.mixmicro.components.enhance.apidoc.provider;

import xyz.vopen.mixmicro.components.enhance.apidoc.model.ClassNodeProxy;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.FieldModel;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.FieldNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class DocFieldProvider implements IFieldProvider {

  @Override
  public List<FieldModel> provideFields(ClassNodeProxy respNode) {
    List<FieldNode> recordNodes = respNode.getChildNodes();
    if (recordNodes == null || recordNodes.isEmpty()) {
      return new ArrayList<>();
    }
    List<FieldModel> entryFieldList = new ArrayList<>();
    FieldModel entryField;
    for (FieldNode recordNode : recordNodes) {
      entryField = new FieldModel();
      String fieldName = DocFieldHelper.getPrefFieldName(recordNode.getName());
      entryField.setCaseFieldName(CommonUtils.capitalize(fieldName));
      entryField.setFieldName(fieldName);
      entryField.setFieldType(DocFieldHelper.getPrefFieldType(recordNode.getType()));
      entryField.setRemoteFieldName(recordNode.getName());
      entryField.setComment(recordNode.getDescription());
      entryFieldList.add(entryField);
    }
    return entryFieldList;
  }
}
