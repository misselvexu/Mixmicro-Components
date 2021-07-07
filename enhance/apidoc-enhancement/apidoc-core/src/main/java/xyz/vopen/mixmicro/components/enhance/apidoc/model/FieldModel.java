package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import xyz.vopen.mixmicro.components.enhance.apidoc.provider.DocFieldHelper;

import java.io.Serializable;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class FieldModel implements Serializable {
  /** file name */
  private String remoteFieldName;
  /** case field name */
  private String caseFieldName;
  /** field name */
  private String fieldName;
  /** field type */
  private String fieldType;
  /** field comment */
  private String comment;

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldType() {
    return fieldType;
  }

  public String getIFieldType() {
    return DocFieldHelper.getIosFieldType(fieldType);
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public String getRemoteFieldName() {
    return remoteFieldName;
  }

  public void setRemoteFieldName(String remoteFieldName) {
    this.remoteFieldName = remoteFieldName;
  }

  public String getCaseFieldName() {
    return caseFieldName;
  }

  public void setCaseFieldName(String caseFieldName) {
    this.caseFieldName = caseFieldName;
  }

  public String getComment() {
    return comment == null ? "" : comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getAssign() {
    if (fieldType != null && fieldType.equalsIgnoreCase("string")) {
      return "assign";
    } else {
      return "strong";
    }
  }
}
