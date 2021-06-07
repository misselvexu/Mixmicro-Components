package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import java.util.List;

/**
 * class node proxy to resolve cycle dependency problem
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/4
 */
public interface ClassNodeProxy {

  String getDescription();

  void setDescription(String description);

  Boolean isList();

  void setList(Boolean list);

  List<FieldNode> getChildNodes();

  void setChildNodes(List<FieldNode> childNodes);

  void addChildNode(FieldNode fieldNode);

  String getClassName();

  void setClassName(String className);

  List<GenericNode> getGenericNodes();

  void setGenericNodes(List<GenericNode> genericNodes);

  void addGenericNode(GenericNode genericNode);

  GenericNode getGenericNode(int index);

  String getClassFileName();

  void setClassFileName(String classFileName);

  ClassNodeProxy getParentNode();

  void setParentNode(ClassNodeProxy classNodeProxy);

  Boolean getShowFieldNotNull();

  void setShowFieldNotNull(Boolean showFieldNotNull);

  Class<?> getModelClass();

  void setModelClass(Class<?> modelClass);

  GenericNode getGenericNode(String type);

  String toJsonApi();
}
