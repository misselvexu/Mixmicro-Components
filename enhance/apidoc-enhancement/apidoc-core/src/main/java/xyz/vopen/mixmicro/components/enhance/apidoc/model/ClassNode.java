package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * class node
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class ClassNode implements Serializable, ClassNodeProxy {
  /** class name */
  private String className = "";
  /** class for reflection */
  private Class<?> modelClass;
  /** class description */
  private String description;
  /** class it list,default false */
  private Boolean isList = Boolean.FALSE;
  /** class child nodes */
  private List<FieldNode> childNodes = new ArrayList<>();
  /** class generic nodes */
  @Transient private List<GenericNode> genericNodes = new ArrayList<>();
  /** class ParentNode{ //parentNode; ClassNode node; } */
  private ClassNodeProxy parentNode;
  /** class file name */
  private String classFileName;
  /** show field not null */
  private Boolean showFieldNotNull = Boolean.FALSE;

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public Boolean isList() {
    return isList;
  }

  @Override
  public void setList(Boolean list) {
    isList = list;
  }

  @Override
  public List<FieldNode> getChildNodes() {
    return childNodes.stream().map(FieldNode.class::cast).collect(Collectors.toList());
  }

  @Override
  public void setChildNodes(List<FieldNode> childNodes) {
    this.childNodes = childNodes;
  }

  @Override
  public void addChildNode(FieldNode fieldNode) {
    childNodes.add(fieldNode);
  }

  @Override
  public String getClassName() {
    return className;
  }

  @Override
  public void setClassName(String className) {
    this.className = className;
  }

  @Override
  public List<GenericNode> getGenericNodes() {
    return genericNodes;
  }

  @Override
  public void setGenericNodes(List<GenericNode> genericNodes) {
    this.genericNodes = genericNodes;
  }

  @Override
  public void addGenericNode(GenericNode genericNode) {
    this.genericNodes.add(genericNode);
  }

  @Override
  public GenericNode getGenericNode(int index) {
    return genericNodes.get(index);
  }

  @Override
  public String getClassFileName() {
    return classFileName;
  }

  @Override
  public void setClassFileName(String classFileName) {
    this.classFileName = classFileName;
  }

  @Override
  public ClassNodeProxy getParentNode() {
    return parentNode;
  }

  @Override
  public void setParentNode(ClassNodeProxy parentNode) {
    this.parentNode = parentNode;
  }

  @Override
  public Boolean getShowFieldNotNull() {
    return showFieldNotNull;
  }

  @Override
  public void setShowFieldNotNull(Boolean showFieldNotNull) {
    this.showFieldNotNull = showFieldNotNull;
  }

  @Override
  public Class<?> getModelClass() {
    return modelClass;
  }

  @Override
  public void setModelClass(Class<?> modelClass) {
    this.modelClass = modelClass;
  }

  @Override
  public GenericNode getGenericNode(String type) {
    if (genericNodes == null) {
      return null;
    }
    for (GenericNode genericNode : genericNodes) {
      if (genericNode.getPlaceholder().equals(type)) {
        return genericNode;
      }
    }
    return null;
  }

  @Override
  public String toJsonApi() {
    if (childNodes == null || childNodes.isEmpty()) {
      return Boolean.TRUE.equals(isList) ? className + "[]" : className + "{}";
    }
    Map<String, Object> jsonRootMap = new LinkedHashMap<>();
    for (FieldNode fieldNode : childNodes) {
      toJsonApiMap(fieldNode, jsonRootMap);
    }
    if (Boolean.TRUE.equals(isList)) {
      return JSON.toJSONString(new Map[] {jsonRootMap}, true);
    } else {
      return JSON.toJSONString(jsonRootMap, true);
    }
  }

  private void toJsonApiMap(FieldNode fieldNode, Map<String, Object> map) {

    if (Boolean.TRUE.equals(fieldNode.getLoopNode())) {
      map.put(fieldNode.getName(), getFieldDesc(fieldNode));
      return;
    }

    ClassNodeProxy thisFieldNode = fieldNode.getChildNode();
    if (thisFieldNode != null) {
      Map<String, Object> childMap = new LinkedHashMap<>();
      for (FieldNode childFieldNode : thisFieldNode.getChildNodes()) {
        if (childFieldNode.getChildNode() != null) {
          toJsonApiMap(childFieldNode, childMap);
        } else {
          childMap.put(childFieldNode.getName(), getFieldDesc(childFieldNode));
        }
      }
      if (fieldNode.getType() != null && fieldNode.getType().endsWith("[]")) {
        map.put(fieldNode.getName(), childMap.isEmpty() ? new Map[] {} : new Map[] {childMap});
      } else {
        map.put(fieldNode.getName(), childMap);
      }
    } else {
      map.put(fieldNode.getName(), getFieldDesc(fieldNode));
    }
  }

  private String getFieldDesc(FieldNode fieldNode) {
    String fieldType;
    if (Boolean.TRUE.equals(fieldNode.getLoopNode())) {
      fieldType =
          fieldNode.getChildNode().getClassName()
              + (Boolean.TRUE.equals(fieldNode.getChildNode().isList()) ? "[]" : "{}");
    } else {
      fieldType = fieldNode.getType();
    }

    String fieldDesc;
    if (StringUtils.isNotEmpty(fieldNode.getDescription())) {
      fieldDesc = String.format("%s //%s", fieldType, fieldNode.getDescription());
    } else {
      fieldDesc = fieldType;
    }
    if (showFieldNotNull && fieldNode.getNotNull() && null != DocContext.getI18n()) {
      fieldDesc =
          String.format("%s【%s】", fieldDesc, DocContext.getI18n().getMessage("parameterNeed"));
    }
    return fieldDesc;
  }

  public void reset() {
    if (null != this.childNodes) {
      this.childNodes.clear();
    } else {
      this.childNodes = new ArrayList<>();
    }
    if (null != this.genericNodes) {
      this.genericNodes.clear();
    } else {
      this.genericNodes = new ArrayList<>();
    }
  }
}
