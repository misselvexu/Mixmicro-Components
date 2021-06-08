package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * filed class node
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/7
 */
public class FieldClassNode implements Serializable {
  /** class name */
  private String fieldClassName;
  /** class for reflection */
  private Class<?> modelClass;
  /** class description */
  private String description;
  /** class it list,default false */
  private Boolean isList = Boolean.FALSE;
  /** class child nodes */
  private List<FieldChildNode> childNodes = new ArrayList<>();
  /** class ParentNode{ //parentNode; ClassNode node; } */
  @Transient private List<GenericNode> genericNodes = new ArrayList<>();
  /** parent field parent class node */
  @Transient private FieldClassNode parentNode;
  /** class file name */
  private String classFileName;
  /** show field not null */
  private Boolean showFieldNotNull = Boolean.FALSE;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean isList() {
    return isList;
  }

  public void setList(Boolean list) {
    isList = list;
  }

  public List<FieldNode> getChildNodes() {
    if (CollectionUtils.isEmpty(childNodes)) {
      return new ArrayList<>();
    }
    return childNodes.stream()
        .map(
            fieldChildNode -> {
              FieldNode fieldNode = new FieldNode();
              BeanUtils.copyProperties(fieldChildNode, fieldNode);
              return fieldNode;
            })
        .collect(Collectors.toList());
  }

  public void setChildNodes(List<FieldChildNode> childNodes) {
    this.childNodes = childNodes;
  }

  public void setFieldChildNodes(List<FieldNode> childNodes) {
    if (CollectionUtils.isEmpty(childNodes)) {
      return;
    }
    this.childNodes =
        childNodes.stream()
            .map(
                childNode -> {
                  FieldChildNode fieldChildNode = new FieldChildNode();
                  BeanUtils.copyProperties(childNode, fieldChildNode);
                  return fieldChildNode;
                })
            .collect(Collectors.toList());
  }

  public void addChildNode(FieldChildNode fieldNode) {
    childNodes.add(fieldNode);
  }

  public String getFieldClassName() {
    return fieldClassName;
  }

  public void setFieldClassName(String fieldClassName) {
    this.fieldClassName = fieldClassName;
  }

  public String getClassFileName() {
    return classFileName;
  }

  public void setClassFileName(String classFileName) {
    this.classFileName = classFileName;
  }

  public FieldClassNode getParentNode() {
    return parentNode;
  }

  public ClassNode getClassParentNode() {
    if (null == parentNode) {
      return null;
    }
    ClassNode classNode = new ClassNode();
    classNode.setChildNodes(parentNode.getChildNodes());
    classNode.setShowFieldNotNull(parentNode.getShowFieldNotNull());
    classNode.setParentNode(parentNode.getClassParentNode());
    classNode.setClassName(parentNode.getFieldClassName());
    classNode.setDescription(parentNode.getDescription());
    classNode.setClassFileName(parentNode.getClassFileName());
    classNode.setList(parentNode.isList());
    classNode.setGenericNodes(parentNode.getGenericNodes());
    classNode.setModelClass(parentNode.getModelClass());
    return classNode;
  }

  public void setParentNode(FieldClassNode parentNode) {
    this.parentNode = parentNode;
  }

  public Boolean getShowFieldNotNull() {
    return showFieldNotNull;
  }

  public void setShowFieldNotNull(Boolean showFieldNotNull) {
    this.showFieldNotNull = showFieldNotNull;
  }

  public Class<?> getModelClass() {
    return modelClass;
  }

  public void setModelClass(Class<?> modelClass) {
    this.modelClass = modelClass;
  }

  public List<GenericNode> getGenericNodes() {
    return genericNodes;
  }

  public void setGenericNodes(List<GenericNode> genericNodes) {
    this.genericNodes = genericNodes;
  }

  public void addGenericNode(GenericNode genericNode) {
    this.genericNodes.add(genericNode);
  }

  public GenericNode getGenericNode(int index) {
    return genericNodes.get(index);
  }

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
