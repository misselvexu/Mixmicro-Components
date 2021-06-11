package xyz.vopen.mixmicro.components.enhance.apidoc.parser;

import com.alibaba.fastjson.JSON;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.javadoc.JavadocBlockTag;
import org.apache.commons.lang3.StringUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.annotations.ApiDoc;
import xyz.vopen.mixmicro.components.enhance.apidoc.annotations.Ignore;
import xyz.vopen.mixmicro.components.enhance.apidoc.consts.ChangeFlag;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.*;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.ParseUtils;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * parse Controller Java the common part, get all request nodes
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public abstract class AbsControllerParser {

  private ControllerNode controllerNode;
  private File javaFile;
  private static final Set<String> ignoreTypeNames = new HashSet<>();

  static {
    ignoreTypeNames.add("annotation");
    ignoreTypeNames.add("void");
    ignoreTypeNames.add("String");
  }

  public ControllerNode parse(File javaFile) {

    this.javaFile = javaFile;
    CompilationUnit compilationUnit = ParseUtils.compilationUnit(javaFile);
    this.controllerNode = new ControllerNode();

    String controllerName = CommonUtils.getJavaFileName(javaFile);
    controllerNode.setClassName(controllerName);
    compilationUnit
        .getClassByName(controllerName)
        .ifPresent(
            c -> {
              beforeHandleController(controllerNode, c);
              parseClassDoc(c);
              parseMethodDocs(c);
              afterHandleController(controllerNode, c);
            });

    return controllerNode;
  }

  File getControllerFile() {
    return javaFile;
  }

  ControllerNode getControllerNode() {
    return controllerNode;
  }

  private void parseClassDoc(ClassOrInterfaceDeclaration c) {
    Optional<Node> parentNode = c.getParentNode();
    parentNode
        .flatMap(node -> node.findFirst(PackageDeclaration.class))
        .ifPresent(pd -> controllerNode.setPackageName(pd.getNameAsString()));
    boolean generateDocs = c.getAnnotationByName("ApiDoc").isPresent();
    controllerNode.setGenerateDocs(generateDocs);
    c.getJavadoc()
        .ifPresent(
            d -> {
              String description = d.getDescription().toText();
              controllerNode.setDescription(
                  StringUtils.isNotEmpty(description) ? description : c.getNameAsString());
              List<JavadocBlockTag> blockTags = d.getBlockTags();
              if (blockTags != null) {
                for (JavadocBlockTag blockTag : blockTags) {
                  if ("author".equalsIgnoreCase(blockTag.getTagName())) {
                    controllerNode.setAuthor(blockTag.getContent().toText());
                  }
                  if ("description".equalsIgnoreCase(blockTag.getTagName())) {
                    controllerNode.setDescription(blockTag.getContent().toText());
                  }
                }
              }
            });

    if (controllerNode.getDescription() == null) {
      controllerNode.setDescription(c.getNameAsString());
    }
  }

  private void parseMethodDocs(ClassOrInterfaceDeclaration c) {
    c.findAll(MethodDeclaration.class).stream()
        .filter(methodDeclaration -> methodDeclaration.getModifiers().contains(Modifier.PUBLIC))
        .forEach(
            m -> {
              boolean existsApiDoc =
                  m.getAnnotationByName(ApiDoc.class.getSimpleName()).isPresent();
              if (!existsApiDoc
                  && Boolean.FALSE.equals(controllerNode.getGenerateDocs())
                  && Boolean.FALSE.equals(DocContext.getDocsConfig().getAutoGenerate())) {
                return;
              }
              if (shouldIgnoreMethod(m)) {
                return;
              }
              RequestNode requestNode = new RequestNode();
              requestNode.setControllerNode(controllerNode);
              requestNode.setAuthor(controllerNode.getAuthor());
              requestNode.setMethodName(m.getNameAsString());
              requestNode.setUrl(requestNode.getMethodName());
              requestNode.setDescription(requestNode.getMethodName());
              m.getAnnotationByClass(Deprecated.class)
                  .ifPresent(f -> requestNode.setDeprecated(true));
              m.getJavadoc()
                  .ifPresent(
                      d -> {
                        String description = d.getDescription().toText();
                        requestNode.setDescription(description);

                        List<JavadocBlockTag> blockTagList = d.getBlockTags();
                        for (JavadocBlockTag blockTag : blockTagList) {
                          if (blockTag.getTagName().equalsIgnoreCase("param")) {
                            ParamNode paramNode = new ParamNode();
                            if (blockTag.getName().isPresent()) {
                              paramNode.setName(blockTag.getName().get());
                            }
                            paramNode.setDescription(blockTag.getContent().toText());
                            requestNode.addParamNode(paramNode);
                          } else if (blockTag.getTagName().equalsIgnoreCase("author")) {
                            requestNode.setAuthor(blockTag.getContent().toText());
                          } else if (blockTag.getTagName().equalsIgnoreCase("description")) {
                            requestNode.setSupplement(blockTag.getContent().toText());
                          } else if (blockTag.getTagName().equalsIgnoreCase("return")) {
                            ResponseNode responseNode = requestNode.getResponseNode();
                            if (null == responseNode) {
                              responseNode = new ResponseNode();
                            }
                            responseNode.setDescription(blockTag.getContent().toText());
                            requestNode.setResponseNode(responseNode);
                          }
                        }
                      });
              m.getParameters()
                  .forEach(
                      parameter -> {
                        String paraName = parameter.getName().asString();
                        ParamNode paramNode = requestNode.getParamNodeByName(paraName);

                        if (paramNode != null && ParseUtils.isExcludeParam(parameter)) {
                          requestNode.getParamNodes().remove(paramNode);
                          return;
                        }

                        if (paramNode != null) {
                          Type pType = parameter.getType();
                          TypeAlsResponse typeAlsResponse = alsType(pType);
                          paramNode.setList(typeAlsResponse.getIsArray());
                          paramNode.setGenericNode(typeAlsResponse.getGenericClassNode());
                          paramNode.setType(typeAlsResponse.getType());
                        }
                      });
              com.github.javaparser.ast.type.Type resultClassType = null;
              String stringResult = null;
              if (existsApiDoc) {
                Optional<AnnotationExpr> apiDoc = m.getAnnotationByName("ApiDoc");
                if (apiDoc.isPresent()) {
                  AnnotationExpr an = apiDoc.get();
                  if (an instanceof SingleMemberAnnotationExpr) {
                    resultClassType =
                        ((ClassExpr) ((SingleMemberAnnotationExpr) an).getMemberValue()).getType();
                  } else if (an instanceof NormalAnnotationExpr) {
                    for (MemberValuePair pair : ((NormalAnnotationExpr) an).getPairs()) {
                      final String pairName = pair.getNameAsString();
                      if ("result".equals(pairName) || "value".equals(pairName)) {
                        resultClassType = ((ClassExpr) pair.getValue()).getType();
                      } else if (pairName.equals("url")) {
                        requestNode.setUrl(((StringLiteralExpr) pair.getValue()).getValue());
                      } else if (pairName.equals("method")) {
                        requestNode.addMethod(((StringLiteralExpr) pair.getValue()).getValue());
                      } else if ("stringResult".equals(pairName)) {
                        stringResult = ((StringLiteralExpr) pair.getValue()).getValue();
                      }
                    }
                  }
                }
              }
              afterHandleMethod(requestNode, m);
              if (resultClassType == null) {
                if (m.getType() == null) {
                  return;
                }
                resultClassType = m.getType();
              }
              ResponseNode responseNode = requestNode.getResponseNode();
              if (null == responseNode) {
                responseNode = new ResponseNode();
              }
              responseNode.setControllerClassName(requestNode.getControllerNode().getClassName());
              responseNode.setMethodName(requestNode.getMethodName());
              responseNode.setRequestUrl(requestNode.getUrl());
              responseNode.setControllerPackageName(
                  requestNode.getControllerNode().getPackageName());
              Type resultClassTypeElementType = resultClassType.getElementType();
              TypeAlsResponse typeAlsResponse = alsType(resultClassTypeElementType);
              responseNode.setList(typeAlsResponse.getIsArray());
              responseNode.setGenericNode(typeAlsResponse.getGenericClassNode());
              responseNode.setType(typeAlsResponse.getType());
              if (stringResult != null) {
                responseNode.setStringResult(stringResult);
              } else {
                handleResponseNode(responseNode, resultClassType.getElementType());
              }
              requestNode.setResponseNode(responseNode);
              setRequestNodeChangeFlag(requestNode);
              controllerNode.addRequestNode(requestNode);
            });
  }

  /**
   * called before controller node has handled
   *
   * @param clazz ClassOrInterfaceDeclaration
   */
  protected void beforeHandleController(
      ControllerNode controllerNode, ClassOrInterfaceDeclaration clazz) {}

  /**
   * called after controller node has handled
   *
   * @param clazz ClassOrInterfaceDeclaration
   */
  protected void afterHandleController(
      ControllerNode controllerNode, ClassOrInterfaceDeclaration clazz) {}

  protected boolean shouldIgnoreMethod(MethodDeclaration m) {
    return m.getAnnotationByName(Ignore.class.getSimpleName()).isPresent();
  }

  /**
   * handle response object
   *
   * @param responseNode ResponseNode
   * @param resultType result type
   */
  protected void handleResponseNode(
      ResponseNode responseNode, com.github.javaparser.ast.type.Type resultType) {
    parseClassNodeByType(responseNode, resultType);
  }

  void parseClassNodeByType(ClassNode classNode, com.github.javaparser.ast.type.Type classType) {
    // maybe void
    if (classType instanceof ClassOrInterfaceType) {
      // 解析方法返回类的泛型信息
      ((ClassOrInterfaceType) classType)
          .getTypeArguments()
          .ifPresent(
              typeList ->
                  typeList.forEach(
                      argType -> {
                        GenericNode rootGenericNode = new GenericNode();
                        rootGenericNode.setFromJavaFile(javaFile);
                        rootGenericNode.setClassType(argType);
                        classNode.addGenericNode(rootGenericNode);
                      }));
      ParseUtils.parseClassNodeByType(javaFile, classNode, classType);
    }
  }

  /** called after request method node has handled */
  protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {}

  // 设置接口的类型（新/修改/一样）
  private void setRequestNodeChangeFlag(RequestNode requestNode) {
    List<ControllerNode> lastControllerNodeList = DocContext.getLastVersionControllerNodes();
    if (lastControllerNodeList == null || lastControllerNodeList.isEmpty()) {
      return;
    }

    for (ControllerNode lastControllerNode : lastControllerNodeList) {
      for (RequestNode lastRequestNode : lastControllerNode.getRequestNodes()) {
        if (lastRequestNode.getUrl().equals(requestNode.getUrl())) {
          requestNode.setLastRequestNode(lastRequestNode);
          requestNode.setChangeFlag(
              isSameRequestNodes(requestNode, lastRequestNode)
                  ? ChangeFlag.SAME
                  : ChangeFlag.MODIFY);
          return;
        }
      }
    }

    requestNode.setChangeFlag(ChangeFlag.NEW);
  }

  private boolean isSameRequestNodes(RequestNode requestNode, RequestNode lastRequestNode) {

    for (String lastMethod : lastRequestNode.getMethod()) {
      if (!requestNode.getMethod().contains(lastMethod)) {
        return false;
      }
    }

    return JSON.toJSONString(requestNode.getParamNodes())
            .equals(JSON.toJSONString(lastRequestNode.getParamNodes()))
        && JSON.toJSONString(requestNode.getHeader())
            .equals(JSON.toJSONString(lastRequestNode.getHeader()))
        && requestNode
            .getResponseNode()
            .toJsonApi()
            .equals(lastRequestNode.getResponseNode().toJsonApi());
  }

  /**
   * generic type analyse
   *
   * @param type class type
   * @return TypeAlsResponse
   */
  private TypeAlsResponse alsType(Type type) {
    TypeAlsResponse typeAlsResponse = new TypeAlsResponse();
    String name = type.asString();
    if (ignoreTypeNames.contains(name)) {
      return typeAlsResponse;
    }
    boolean isList = false;
    if (type instanceof ArrayType) {
      isList = true;
      type = ((ArrayType) type).getComponentType();
    } else if (ParseUtils.isCollectionType(type.asString())) {
      isList = true;
      List<ClassOrInterfaceType> collectionTypes = type.findAll(ClassOrInterfaceType.class);
      if (!collectionTypes.isEmpty()) {
        type = collectionTypes.get(0);
      } else {
        typeAlsResponse.setType("Object[]");
      }
      // 解析集合泛型
      ClassOrInterfaceType classOrInterfaceType = collectionTypes.get(1);
      ClassNode classNode = new ClassNode();
      ParseUtils.parseClassNodeByType(javaFile, classNode, classOrInterfaceType);
      typeAlsResponse.setGenericClassNode(classNode);
    }
    if (typeAlsResponse.getType() == null) {
      if (ParseUtils.isEnum(javaFile, type.asString())) {
        typeAlsResponse.setType(isList ? "enum[]" : "enum");
      } else {
        final String pUnifyType = ParseUtils.unifyType(type.asString());
        typeAlsResponse.setType(isList ? pUnifyType + "[]" : pUnifyType);
      }
    }
    typeAlsResponse.setIsArray(isList);
    return typeAlsResponse;
  }
}
