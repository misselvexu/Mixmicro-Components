package xyz.vopen.mixmicro.components.enhance.apidoc.model;

import com.github.javaparser.ast.type.Type;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * generic node of model class
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class GenericNode implements Serializable {
  /** for source */
  private Type classType;
  /** for reflection */
  private Class<?> modelClass;
  /** placeholder */
  private String placeholder;
  /** form java file */
  private File fromJavaFile;
  /** child generic node list */
  private List<GenericNode> childGenericNode = new ArrayList<>();

  public GenericNode() {}

  public Type getClassType() {
    return classType;
  }

  public void setClassType(Type classType) {
    this.classType = classType;
  }

  public File getFromJavaFile() {
    return fromJavaFile;
  }

  public void setFromJavaFile(File fromJavaFile) {
    this.fromJavaFile = fromJavaFile;
  }

  public List<GenericNode> getChildGenericNode() {
    return childGenericNode;
  }

  public void setChildGenericNode(List<GenericNode> childGenericNode) {
    this.childGenericNode = childGenericNode;
  }

  public void addChildGenericNode(GenericNode childNode) {
    this.childGenericNode.add(childNode);
  }

  public String getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
  }

  public Class<?> getModelClass() {
    return modelClass;
  }

  public void setModelClass(Class<?> modelClass) {
    this.modelClass = modelClass;
  }
}
