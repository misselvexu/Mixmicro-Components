package xyz.vopen.mixmicro.components.enhance.apidoc.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.annotations.Ignore;
import xyz.vopen.mixmicro.components.enhance.apidoc.exception.FileParseException;
import xyz.vopen.mixmicro.components.enhance.apidoc.exception.JavaFileNotFoundException;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.*;
import java.util.*;

/**
 * some util methods during parse
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class ParseUtils {

  private ParseUtils() {}

  /** means a model class type */
  private static final String TYPE_MODEL = "_object";

  private static final String OBJECT_CLASS_NAME = "Object";

  private static final String JAVA_FILE_SUFFIX = ".java";

  private static final Logger LOGGER = LoggerFactory.getLogger(ParseUtils.class);

  /**
   * search File of className in the java file
   *
   * @param inJavaFile java file
   * @param className java class name
   * @return file
   */
  public static File searchJavaFile(File inJavaFile, String className) {
    File file = null;

    for (String javaSrcPath : DocContext.getJavaSrcPaths()) {
      file = searchJavaFileInner(javaSrcPath, inJavaFile, className);
      if (file != null) {
        break;
      }
    }

    if (file == null) {
      throw new JavaFileNotFoundException(
          "Cannot find java file , in java file : "
              + inJavaFile.getAbsolutePath()
              + ", className : "
              + className);
    }

    return file;
  }

  private static File searchJavaFileInner(String javaSrcPath, File inJavaFile, String className) {
    CompilationUnit compilationUnit = compilationUnit(inJavaFile);

    String[] cPaths;

    Optional<ImportDeclaration> idOp =
        compilationUnit.getImports().stream()
            .filter(im -> im.getNameAsString().endsWith("." + className))
            .findFirst();

    // found in import
    if (idOp.isPresent()) {
      cPaths = idOp.get().getNameAsString().split("\\.");
      return backTraceJavaFileByName(javaSrcPath, cPaths);
    }

    // inner class in this file
    if (getInnerClassNode(compilationUnit, className).isPresent()) {
      return inJavaFile;
    }

    cPaths = className.split("\\.");

    // current directory
    if (cPaths.length == 1) {

      File[] javaFiles =
          inJavaFile
              .getParentFile()
              .listFiles((dir, name) -> name.equals(className + JAVA_FILE_SUFFIX));

      if (javaFiles != null && javaFiles.length == 1) {
        return javaFiles[0];
      }

    } else {

      final String firstPath = cPaths[0];
      // same package inner class
      File[] javaFiles =
          inJavaFile
              .getParentFile()
              .listFiles(
                  (dir, name) -> {
                    int i = name.lastIndexOf(JAVA_FILE_SUFFIX);
                    if (i == -1) {
                      return false;
                    }
                    return name.substring(0, i).equals(firstPath);
                  });

      if (javaFiles != null && javaFiles.length > 0) {
        File javaFile = javaFiles[0];
        if (getInnerClassNode(compilationUnit(javaFile), className).isPresent()) {
          return javaFile;
        }
      }
    }

    // maybe a complete class name
    File javaFile = backTraceJavaFileByName(javaSrcPath, cPaths);
    if (javaFile != null) {
      return javaFile;
    }

    // .* at import
    NodeList<ImportDeclaration> importDeclarations = compilationUnit.getImports();
    if (importDeclarations.isNonEmpty()) {
      for (ImportDeclaration importDeclaration : importDeclarations) {
        if (importDeclaration.toString().contains(".*")) {
          String packageName = importDeclaration.getNameAsString();
          cPaths = (packageName + "." + className).split("\\.");
          javaFile = backTraceJavaFileByName(javaSrcPath, cPaths);
          if (javaFile != null) {
            break;
          }
        }
      }
    }

    // inner class in other package
    if (cPaths.length > 1) {
      try {
        File innerClassFile = searchJavaFile(inJavaFile, cPaths[cPaths.length - 2]);
        if (getInnerClassNode(compilationUnit(innerClassFile), cPaths[cPaths.length - 1])
            .isPresent()) {
          return innerClassFile;
        }
      } catch (JavaFileNotFoundException ex) {
        // just ignore
      }
    }

    return javaFile;
  }

  /**
   * get inner class node
   *
   * @param compilationUnit compilation unit
   * @param className class name
   * @return TypeDeclaration Optional
   */
  @SuppressWarnings("rawtypes")
  private static Optional<TypeDeclaration> getInnerClassNode(
      CompilationUnit compilationUnit, String className) {
    return compilationUnit.findAll(TypeDeclaration.class).stream()
        .filter(c -> c instanceof ClassOrInterfaceDeclaration || c instanceof EnumDeclaration)
        .filter(c -> className.equals(c.getNameAsString()))
        .findFirst();
  }

  private static File backTraceJavaFileByName(String javaSrcPath, String[] cPaths) {
    if (cPaths.length == 0) {
      return null;
    }
    String javaFilePath = javaSrcPath + CommonUtils.joinArrayString(cPaths, "/") + JAVA_FILE_SUFFIX;
    File javaFile = new File(javaFilePath);
    if (javaFile.exists() && javaFile.isFile()) {
      return javaFile;
    } else {
      return backTraceJavaFileByName(javaSrcPath, Arrays.copyOf(cPaths, cPaths.length - 1));
    }
  }

  /**
   * get java file parser object
   *
   * @param javaFile java file
   * @return compilation unit
   */
  public static CompilationUnit compilationUnit(File javaFile) {
    try {
      return JavaParser.parse(javaFile);
    } catch (FileNotFoundException e) {
      throw new FileParseException(
          "java file not exits , file path : " + javaFile.getAbsolutePath());
    } catch (Exception e) {
      throw new FileParseException("parser error , file path : " + javaFile.getAbsolutePath());
    }
  }

  /**
   * parse class model java file
   *
   * @param inJavaFile java file
   * @param classType 携带了类的泛型信息
   */
  public static void parseClassNodeByType(
      File inJavaFile, ClassNode rootClassNode, com.github.javaparser.ast.type.Type classType) {
    Optional<Node> nodeOptional = classType.getParentNode();
    if (nodeOptional.isPresent() && nodeOptional.get() instanceof ArrayType) {
      rootClassNode.setList(true);
    } else if (classType instanceof ArrayType) {
      rootClassNode.setList(true);
      classType = ((ArrayType) classType).getComponentType();
    } else if (isCollectionType(classType.asString())) {
      rootClassNode.setList(true);
      rootClassNode.setGenericNodes(null);
      List<ClassOrInterfaceType> collectionType = classType.findAll(ClassOrInterfaceType.class);
      if (collectionType.isEmpty()) {
        LOGGER.warn(
            "We found Collection without specified Class Type, Please check ! java file : {}",
            inJavaFile.getName());
        rootClassNode.setClassName(OBJECT_CLASS_NAME);
        return;
      } else {
        classType = collectionType.get(0);
      }
    }

    String unifyClassType = unifyType(classType.asString());
    if (TYPE_MODEL.equals(unifyClassType)) {
      if (classType instanceof ClassOrInterfaceType) {

        String className = ((ClassOrInterfaceType) classType).getName().getIdentifier();
        rootClassNode.setClassName(className);

        try {
          File modelJavaFile = searchJavaFile(inJavaFile, className);
          rootClassNode.setClassFileName(modelJavaFile.getAbsolutePath());
          parseClassNode(modelJavaFile, rootClassNode);
        } catch (JavaFileNotFoundException ex) {
          parseResponseNodeByReflection(inJavaFile, className, rootClassNode);
        }
      }
    } else {
      rootClassNode.setClassName(unifyClassType);
    }
  }

  /**
   * parse class model java file
   *
   * @param modelJavaFile java model file
   * @param classNode class node
   */
  public static void parseClassNode(File modelJavaFile, ClassNode classNode) {
    innerParseClassNode(modelJavaFile, classNode);
  }

  private static void innerParseClassNode(File modelJavaFile, ClassNode classNode) {
    String resultClassName = classNode.getClassName();
    ParseUtils.compilationUnit(modelJavaFile).findAll(ClassOrInterfaceDeclaration.class).stream()
        .filter(f -> resultClassName.endsWith(f.getNameAsString()))
        .findFirst()
        .ifPresent(
            cl -> {

              // handle generic type
              NodeList<TypeParameter> typeParameters = cl.getTypeParameters();
              if (typeParameters.isNonEmpty()
                  && classNode.getGenericNodes().size() == typeParameters.size()) {
                for (int i = 0, len = typeParameters.size(); i != len; i++) {
                  // <T> or  <T extends Serializable>
                  classNode
                      .getGenericNode(i)
                      .setPlaceholder(typeParameters.get(i).getName().getIdentifier());
                }
              }

              NodeList<ClassOrInterfaceType> extendClassTypeList = cl.getExtendedTypes();

              // 解析继承类
              if (!extendClassTypeList.isEmpty()) {
                // 子类传递泛型给父类的情形 class C<T> extends B<T> 或者 class C<T> extends B<Student>
                ClassNode extendClassNode = new ClassNode();
                List<GenericNode> extendGenericNodes = new ArrayList<>();
                extendClassNode.setGenericNodes(extendGenericNodes); // 使用新的泛型列表
                ClassOrInterfaceType extendClassType = extendClassTypeList.get(0);
                extendClassType
                    .getTypeArguments()
                    .ifPresent(
                        typeList ->
                            typeList.forEach(
                                argType -> {
                                  GenericNode extendGenericNode = new GenericNode();
                                  GenericNode thisGenericNode =
                                      classNode.getGenericNode(argType.asString());
                                  if (thisGenericNode != null) { // 拷贝一份，防止placeholder发生变化
                                    extendGenericNode.setFromJavaFile(
                                        thisGenericNode.getFromJavaFile());
                                    extendGenericNode.setClassType(thisGenericNode.getClassType());
                                  } else {
                                    extendGenericNode.setFromJavaFile(modelJavaFile);
                                    extendGenericNode.setClassType(argType);
                                  }
                                  extendGenericNodes.add(extendGenericNode);
                                }));
                ParseUtils.parseClassNodeByType(modelJavaFile, extendClassNode, extendClassType);
                // 把解析结果复制到子类
                classNode.getChildNodes().addAll(extendClassNode.getChildNodes());
              }

              cl.findAll(FieldDeclaration.class).stream()
                  .filter(
                      fd -> !fd.getModifiers().contains(com.github.javaparser.ast.Modifier.STATIC))
                  .forEach(
                      fd -> {

                        // 内部类字段也会读取到，这里特殊处理
                        Optional<Node> parentNode = fd.getParentNode();
                        if (parentNode.isPresent()) {
                          Node cClNode = parentNode.get();
                          if (cClNode instanceof TypeDeclaration) {
                            TypeDeclaration<?> cClDeclaration = (TypeDeclaration<?>) cClNode;
                            if (!(resultClassName.equals(cClDeclaration.getNameAsString())
                                || resultClassName.endsWith(
                                    "." + cClDeclaration.getNameAsString()))) {
                              return;
                            }
                          }
                        }

                        // 忽略字段
                        if (fd.getAnnotationByName(Ignore.class.getSimpleName()).isPresent()) {
                          return;
                        }

                        final boolean notNull = isFieldNotNull(fd);

                        fd.getVariables()
                            .forEach(
                                field -> {
                                  FieldNode fieldNode = new FieldNode();
                                  fieldNode.setNotNull(notNull);
                                  fieldNode.setClassNode(classNode);

                                  classNode.addChildNode(fieldNode);
                                  fd.getComment()
                                      .ifPresent(
                                          c ->
                                              fieldNode.setDescription(
                                                  CommonUtils.cleanCommentContent(c.getContent())));

                                  if (StringUtils.isEmpty(fieldNode.getDescription())) {
                                    field
                                        .getComment()
                                        .ifPresent(
                                            c ->
                                                fieldNode.setDescription(
                                                    CommonUtils.cleanCommentContent(
                                                        c.getContent())));
                                  }

                                  fd.getAnnotationByName("RapMock")
                                      .ifPresent(
                                          an -> {
                                            if (an instanceof NormalAnnotationExpr) {
                                              NormalAnnotationExpr normalAnExpr =
                                                  (NormalAnnotationExpr) an;
                                              MockNode mockNode = new MockNode();
                                              for (MemberValuePair mvPair :
                                                  normalAnExpr.getPairs()) {
                                                String name = mvPair.getName().asString();
                                                if ("limit".equalsIgnoreCase(name)) {
                                                  mockNode.setLimit(
                                                      CommonUtils.removeQuotations(
                                                          mvPair.getValue().toString()));
                                                } else if ("value".equalsIgnoreCase(name)) {
                                                  mockNode.setValue(
                                                      CommonUtils.removeQuotations(
                                                          mvPair.getValue().toString()));
                                                }
                                              }
                                              fieldNode.setMockNode(mockNode);
                                            } else if (an instanceof SingleMemberAnnotationExpr) {
                                              SingleMemberAnnotationExpr singleAnExpr =
                                                  (SingleMemberAnnotationExpr) an;
                                              MockNode mockNode = new MockNode();
                                              mockNode.setValue(
                                                  CommonUtils.removeQuotations(
                                                      singleAnExpr.getMemberValue().toString()));
                                              fieldNode.setMockNode(mockNode);
                                            }
                                          });

                                  fieldNode.setName(field.getNameAsString());

                                  com.github.javaparser.ast.type.Type fieldType =
                                      fd.getElementType();
                                  parseFieldNode(fieldNode, modelJavaFile, fieldType);
                                });
                      });
            });

    // 恢复原来的名称
    classNode.setClassName(resultClassName);
  }

  private static boolean isFieldNotNull(FieldDeclaration fd) {
    for (AnnotationExpr annotationExpr : fd.getAnnotations()) {
      if (isNotNullAnnotation(annotationExpr.getNameAsString())) {
        return true;
      }
    }
    return false;
  }

  private static void parseFieldNode(
      FieldNode fieldNode, File inJavaFile, com.github.javaparser.ast.type.Type fieldType) {

    final GenericNode genericNode = fieldNode.getClassNode().getGenericNode(fieldType.asString());

    // Enum
    if (genericNode == null && !fieldType.asString().contains("<")) {
      final String fieldClassType = fieldType.asString();
      if (TYPE_MODEL.equals(unifyType(fieldClassType))) {

        Optional<EnumDeclaration> ed = Optional.empty();
        try {
          File childJavaFile = searchJavaFile(inJavaFile, fieldClassType);
          ed =
              compilationUnit(childJavaFile).findAll(EnumDeclaration.class).stream()
                  .filter(em -> fieldClassType.endsWith(em.getNameAsString()))
                  .findFirst();
        } catch (JavaFileNotFoundException ex) {
          LOGGER.info("we think {} should not be an enum type", fieldClassType);
        }
        if (ed.isPresent()) {
          fieldNode.setType("enum");
          List<EnumConstantDeclaration> constants = ed.get().findAll(EnumConstantDeclaration.class);
          StringBuilder sb =
              new StringBuilder(
                  fieldNode.getDescription() == null ? "" : fieldNode.getDescription());
          sb.append(" [");
          for (int i = 0, size = constants.size(); i != size; i++) {
            sb.append(constants.get(i).getNameAsString());
            if (i != size - 1) {
              sb.append(",");
            }
          }
          sb.append("]");
          fieldNode.setDescription(sb.toString());
          return;
        }
      }
    }

    // generic type field, do replacement
    if (genericNode != null) {
      fieldType = genericNode.getClassType();
      inJavaFile = genericNode.getFromJavaFile();
    }

    boolean isList;
    Optional<Node> parentNode = fieldType.getParentNode();
    if (parentNode.isPresent() && parentNode.get() instanceof ArrayType) {
      isList = true;
    } else if (fieldType instanceof ArrayType) {
      isList = true;
      fieldType = ((ArrayType) fieldType).getComponentType();
      GenericNode arrayTypeNode = fieldNode.getClassNode().getGenericNode(fieldType.asString());
      if (arrayTypeNode != null) {
        fieldType = arrayTypeNode.getClassType();
        inJavaFile = arrayTypeNode.getFromJavaFile();
      }
    } else {
      if (isCollectionType(fieldType.asString())) {
        isList = true;
        List<ClassOrInterfaceType> collectionType = fieldType.findAll(ClassOrInterfaceType.class);
        if (collectionType.isEmpty()) {
          LOGGER.warn(
              "We found Collection without specified Class Type, Please check ! java file : {}",
              inJavaFile.getName());
          fieldNode.setType("Object[]");
          return;
        } else {
          // 是否在泛型列表中
          GenericNode collectionGenericNode =
              fieldNode.getClassNode().getGenericNode(collectionType.get(0).asString());
          if (collectionGenericNode != null) {
            fieldType = collectionGenericNode.getClassType();
            inJavaFile = collectionGenericNode.getFromJavaFile();
          } else {
            fieldType = collectionType.get(0);
          }
        }
      } else {
        isList = false;
      }
    }

    String fieldClassType;

    if (fieldType instanceof ClassOrInterfaceType) {
      fieldClassType = ((ClassOrInterfaceType) fieldType).getName().getIdentifier();
    } else {
      fieldClassType = fieldType.asString();
    }

    final String unifyType = unifyType(fieldClassType);

    if (TYPE_MODEL.equals(unifyType)) {

      ClassNode childNode = new ClassNode();
      childNode.setParentNode(fieldNode.getClassNode());
      childNode.setList(isList);
      childNode.setClassName(fieldClassType);

      fieldNode.setChildNode(childNode);
      fieldNode.setType(Boolean.TRUE.equals(isList) ? fieldClassType + "[]" : fieldClassType);

      final File childJavaFile = inJavaFile;
      assert fieldType instanceof ClassOrInterfaceType;
      ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) fieldType;
      classOrInterfaceType
          .getTypeArguments()
          .ifPresent(
              typeList ->
                  typeList.forEach(
                      argType -> {
                        GenericNode childClassGenericNode = new GenericNode();

                        if (argType instanceof ArrayType) {
                          GenericNode arrayTypeNode =
                              fieldNode
                                  .getClassNode()
                                  .getGenericNode(
                                      ((ArrayType) argType).getComponentType().asString());
                          if (arrayTypeNode != null) {
                            ((ArrayType) argType).setComponentType(arrayTypeNode.getClassType());
                          }
                        } else {
                          GenericNode arrayTypeNode =
                              fieldNode.getClassNode().getGenericNode(argType.asString());
                          if (arrayTypeNode != null) {
                            argType = arrayTypeNode.getClassType();
                          }
                        }

                        childClassGenericNode.setClassType(argType);
                        childClassGenericNode.setFromJavaFile(childJavaFile);
                        childNode.addGenericNode(childClassGenericNode);
                      }));

      try {
        File childNodeJavaFile = searchJavaFile(inJavaFile, fieldClassType);
        childNode.setClassFileName(childNodeJavaFile.getAbsolutePath());
        if (!inClassDependencyTree(fieldNode, fieldNode.getClassNode())) {
          parseClassNode(childNodeJavaFile, childNode);
        } else {
          fieldNode.setLoopNode(Boolean.TRUE);
        }
      } catch (JavaFileNotFoundException ex) {
        LOGGER.warn(
            "we cannot found more information of it, you've better to make it a JavaBean", ex);
        fieldNode.setType(isList ? "Object[]" : OBJECT_CLASS_NAME);
      }
    } else {
      fieldNode.setType(isList ? unifyType + "[]" : unifyType);
    }
  }

  /**
   * 判断本节点是否处在依赖树中，防止出现循环引用
   *
   * @param fieldNode file node
   * @return is dependency tree
   */
  private static boolean inClassDependencyTree(FieldNode fieldNode, ClassNode parentClassNode) {

    if (fieldNode.getChildNode().getModelClass() != null
        && parentClassNode.getModelClass() != null) {
      if (fieldNode.getChildNode().getModelClass().equals(parentClassNode.getModelClass())) {
        return true;
      }
    } else if (fieldNode.getChildNode().getClassFileName() != null
        && fieldNode.getChildNode().getClassFileName().equals(parentClassNode.getClassFileName())
        && fieldNode.getChildNode().getClassName().equals(parentClassNode.getClassName())) {
      return true;
    }

    if (parentClassNode.getParentNode() == null) {
      return false;
    }

    return inClassDependencyTree(fieldNode, parentClassNode.getParentNode());
  }

  /**
   * is model type or not
   *
   * @param className class name
   * @return model type
   */
  public static boolean isModelType(String className) {
    if (className == null) {
      return false;
    }
    return TYPE_MODEL.equals(unifyType(className));
  }

  /**
   * 判断是否是枚举类型
   *
   * @return is enum type
   */
  public static boolean isEnum(File inJavaFile, String className) {
    try {
      File javaFile = searchJavaFile(inJavaFile, className);
      return compilationUnit(javaFile).getEnumByName(className).isPresent();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * unify the type show in docs
   *
   * @param className class name
   * @return type
   */
  public static String unifyType(String className) {
    String[] cPaths = className.replace("[]", "").split("\\.");
    String rawType = cPaths[cPaths.length - 1];
    if ("byte".equalsIgnoreCase(rawType)) {
      return "byte";
    } else if ("short".equalsIgnoreCase(rawType)) {
      return "short";
    } else if ("int".equalsIgnoreCase(rawType)
        || "Integer".equalsIgnoreCase(rawType)
        || "BigInteger".equalsIgnoreCase(rawType)) {
      return "int";
    } else if ("long".equalsIgnoreCase(rawType)) {
      return "long";
    } else if ("float".equalsIgnoreCase(rawType)) {
      return "float";
    } else if ("double".equalsIgnoreCase(rawType) || "BigDecimal".equalsIgnoreCase(rawType)) {
      return "double";
    } else if ("boolean".equalsIgnoreCase(rawType)) {
      return "boolean";
    } else if ("char".equalsIgnoreCase(rawType) || "Character".equalsIgnoreCase(rawType)) {
      return "char";
    } else if ("String".equalsIgnoreCase(rawType)) {
      return "string";
    } else if ("date".equalsIgnoreCase(rawType)
        || "LocalDate".equalsIgnoreCase(rawType)
        || "ZonedDateTime".equalsIgnoreCase(rawType)
        || "LocalDateTime".equals(rawType)) {
      return "date";
    } else if ("file".equalsIgnoreCase(rawType) || "MultipartFile".equalsIgnoreCase(rawType)) {
      return "file";
    } else if (OBJECT_CLASS_NAME.equalsIgnoreCase(rawType)) {
      return "object";
    } else if ("enum".equalsIgnoreCase(rawType)) {
      return "enum";
    } else {
      return TYPE_MODEL;
    }
  }

  /**
   * is implements from Collection or not
   *
   * @param className class name
   * @return is collection
   */
  public static boolean isCollectionType(String className) {
    String[] cPaths = className.split("\\.");

    String genericType = cPaths[cPaths.length - 1];

    // fix List<Demo1.Demo2>
    for (String cPath : cPaths) {
      if (cPath.contains("<")) {
        genericType = cPath;
      }
    }

    int genericLeftIndex = genericType.indexOf("<");
    String rawType =
        genericLeftIndex != -1 ? genericType.substring(0, genericLeftIndex) : genericType;
    String collectionClassName = "java.util." + rawType;
    try {
      Class<?> collectionClass = Class.forName(collectionClassName);
      return Collection.class.isAssignableFrom(collectionClass);
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  /**
   * like HttpServletRequest, HttpServletSession should be auto ignore
   *
   * @param param Parameter
   * @return is exclude param
   */
  public static boolean isExcludeParam(Parameter param) {
    final String type = param.getTypeAsString();
    return type.equals("HttpServletRequest")
        || type.equals("HttpServletResponse")
        || type.equals("HttpSession");
  }

  /**
   * 获取java文件中的类对象
   *
   * @param inJavaFile 所在java文件
   * @param className 类名
   * @return class
   */
  public static Class<?> getClassInJavaFile(File inJavaFile, String className) {
    Class<?> modelClass = null;
    CompilationUnit compilationUnit = compilationUnit(inJavaFile);
    for (ImportDeclaration importDeclaration : compilationUnit.getImports()) {
      String importName = importDeclaration.getNameAsString();
      if (importName.endsWith("." + className)) {
        try {
          modelClass = Class.forName(importName);
          break;
        } catch (Exception e) {
          LOGGER.error("ClassNotFoundException: {}", className);
        }
      } else if (importName.endsWith(".*")) {
        try {
          modelClass = Class.forName(importName.replace("*", className));
        } catch (Exception e) {
          LOGGER.error("reflect model class error modelClass:{}", modelClass);
        }
      }
    }
    Optional<PackageDeclaration> packageDeclaration = compilationUnit.getPackageDeclaration();
    if (packageDeclaration.isPresent()) {
      try {
        modelClass =
            Class.forName(packageDeclaration.get().getNameAsString().concat(".").concat(className));
      } catch (Exception e) {
        LOGGER.error("get model class error,class:{}", className);
      }
    }
    return modelClass;
  }

  private static void parseResponseNodeByReflection(
      File inJavaFile, String className, ClassNode classNode) {
    Class<?> modelClass = getClassInJavaFile(inJavaFile, className);
    if (modelClass != null) {
      if (classNode instanceof ResponseNode) {
        ResponseNode responseNode = (ResponseNode) classNode;
        responseNode.reset();
        // 解析方法返回值泛型信息
        Class<?> controllerClass =
            ParseUtils.getClassInJavaFile(
                inJavaFile, inJavaFile.getName().replace(JAVA_FILE_SUFFIX, ""));
        if (controllerClass != null) {
          try {
            // 获取该api对应的请求方法
            Method apiMethod = null;
            for (Method method : controllerClass.getDeclaredMethods()) {
              if (method.getName().equals(responseNode.getMethodName())
                  && method.getAnnotations() != null) {
                for (Annotation annotation : method.getAnnotations()) {
                  String reqUrl = responseNode.getRequestUrl();
                  if (reqUrl.contains("/")) {
                    reqUrl = reqUrl.substring(reqUrl.lastIndexOf("/") + 1);
                  }
                  if (annotation.toString().contains(reqUrl)) {
                    apiMethod = method;
                  }
                }
              }
            }

            if (apiMethod != null) {
              Type returnType = apiMethod.getGenericReturnType();
              // 如果是ResponseEntity, 则取里面一层
              if (apiMethod
                  .getReturnType()
                  .getName()
                  .equals("org.springframework.http.ResponseEntity")) {
                Type[] responseEntityTypeArguments =
                    ((ParameterizedType) apiMethod.getGenericReturnType()).getActualTypeArguments();
                if (responseEntityTypeArguments.length == 1
                    && responseEntityTypeArguments[0] instanceof ParameterizedType) {
                  ParameterizedType parameterizedType =
                      (ParameterizedType) responseEntityTypeArguments[0];
                  if (parameterizedType.getRawType() instanceof Class) {
                    ParseUtils.parseGenericNodesInType(
                        (Class<?>) parameterizedType.getRawType(),
                        parameterizedType,
                        responseNode.getGenericNodes());
                  }
                }
              } else {
                ParseUtils.parseGenericNodesInType(
                    apiMethod.getReturnType(), returnType, responseNode.getGenericNodes());
              }
            }

          } catch (Exception e2) {
            LOGGER.error("get method error", e2);
          }
        }
      }

      // 使用反射解析字段
      if (Boolean.TRUE.equals(DocContext.getDocsConfig().getOpenReflection())) {
        classNode.setModelClass(modelClass);
        parseClassNodeByReflection(classNode);
      }
    }
  }

  /**
   * 通过反射解析对象字段
   *
   * @param classNode class node
   */
  public static void parseClassNodeByReflection(ClassNode classNode) {

    Class<?> modelClass = classNode.getModelClass();
    // java 内部对象或者接口，直接忽略
    if (modelClass.getPackage().getName().startsWith("java.") || modelClass.isInterface()) {
      return;
    }
    List<FieldNode> childNodes = classNode.getChildNodes();
    classNode.setClassName(modelClass.getName());
    for (Field field : modelClass.getDeclaredFields()) fieldHandler(classNode, childNodes, field);
    // 解析父类
    Class<?> superClass = modelClass.getSuperclass();
    if (superClass != null) {
      Type superType = modelClass.getGenericSuperclass();
      // 父类泛型信息
      if (superType instanceof ParameterizedType) {
        classNode.getGenericNodes().clear();
        parseGenericNodesInType(superClass, superType, classNode.getGenericNodes());
      }
      classNode.setModelClass(modelClass.getSuperclass());
      parseClassNodeByReflection(classNode);
      // 恢复
      classNode.setClassName(modelClass.getName());
      classNode.setModelClass(modelClass);
    }
  }

  /**
   * 解析泛型
   *
   * @param fieldClass file class
   * @param fieldGenericType filed generic type
   * @param genericNodeList generic node list
   */
  public static void parseGenericNodesInType(
      Class<?> fieldClass, Type fieldGenericType, List<GenericNode> genericNodeList) {
    if (fieldGenericType instanceof GenericArrayType) {
      fieldGenericType = ((GenericArrayType) fieldGenericType).getGenericComponentType();
    }
    if (fieldGenericType instanceof ParameterizedType) {
      TypeVariable<?>[] typeVariables = fieldClass.getTypeParameters();
      Type[] paramTypes = ((ParameterizedType) fieldGenericType).getActualTypeArguments();
      if (typeVariables.length == 0 || typeVariables.length != paramTypes.length) {
        return;
      }
      int paramIndex = 0;
      for (Type paramType : paramTypes) {
        GenericNode paramGenericNode = new GenericNode();
        final String placeholder = typeVariables[paramIndex++].getName();
        paramGenericNode.setPlaceholder(placeholder);

        if (paramType instanceof ParameterizedType) { // GenericResult<Student, Integer> picList
          Class<?> boxClass = (Class<?>) ((ParameterizedType) paramType).getRawType();
          paramGenericNode.setModelClass(boxClass);
          genericNodeList.add(paramGenericNode);
          Type[] childTypeArguments = ((ParameterizedType) paramType).getActualTypeArguments();
          TypeVariable<?>[] boxClassTypeVariables = boxClass.getTypeParameters();
          if (childTypeArguments != null) {
            if (childTypeArguments.length == 0
                || childTypeArguments.length != boxClassTypeVariables.length) {
              continue;
            }
            List<GenericNode> childGenericNodeList = new ArrayList<>();
            paramGenericNode.setChildGenericNode(childGenericNodeList);
            int childParamIndex = 0;
            for (Type childGenericType : childTypeArguments) {
              if (childGenericType instanceof Class) {
                GenericNode childGenericNode = new GenericNode();
                childGenericNode.setModelClass((Class<?>) childGenericType);
                childGenericNode.setPlaceholder(boxClassTypeVariables[childParamIndex++].getName());
                childGenericNodeList.add(childGenericNode);
              } else {
                parseGenericNodesInType(boxClass, childGenericType, childGenericNodeList);
              }
            }
          }
        } else if (paramType instanceof Class) { // List<String>
          Class<?> boxClass = (Class<?>) paramType;
          paramGenericNode.setModelClass(boxClass);
          genericNodeList.add(paramGenericNode);
        }
      }
    }
  }

  public static boolean isNotNullAnnotation(String annotationName) {
    return annotationName.endsWith("NotEmpty")
        || annotationName.endsWith("NotNull")
        || annotationName.endsWith("NotBlank");
  }

  /**
   * field type handler
   *
   * @param classNode class node
   * @param childNodes result class nodes
   * @param field filed
   */
  private static void fieldHandler(ClassNode classNode, List<FieldNode> childNodes, Field field) {
    // 过滤 static
    if (Modifier.isStatic(field.getModifiers())) {
      return;
    }
    FieldNode fieldNode = new FieldNode();
    childNodes.add(fieldNode);
    Annotation[] annotations = field.getAnnotations();
    if (annotations != null) {
      for (Annotation annotation : annotations) {
        if (isNotNullAnnotation(annotation.getClass().getSimpleName())) {
          fieldNode.setNotNull(true);
          break;
        }
      }
    }
    fieldNode.setName(field.getName());
    // 枚举,当做字符串类型
    if (field.getType().isEnum()) {
      fieldNode.setType("enum");
      return;
    }
    ClassNode childClassNode = new ClassNode();
    childClassNode.setParentNode(classNode);
    Class<?> childNodeClass = null;
    Type childGenericType = null;
    Class<?> fieldClass = field.getType();
    // 数组
    if (fieldClass.isArray()) {
      Class<?> componentType = fieldClass.getComponentType();
      final String unifyFieldType = unifyType(componentType.getSimpleName());
      fieldNode.setType(unifyFieldType + "[]");
      if (unifyFieldType.equals(TYPE_MODEL)) {
        childClassNode.setList(true);
        childNodeClass = componentType;
        childGenericType = field.getGenericType();
      } else {
        return;
      }
    }
    // 列表
    if (Collection.class.isAssignableFrom(fieldClass)) {
      fieldNode.setType("[]");
      Type genericType = field.getGenericType();
      if (!(genericType instanceof ParameterizedType)) {
        return;
      }
      Type boxType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
      childGenericType = boxType;
      Class<?> boxClass = null;
      if (boxType instanceof ParameterizedType) { // List<GenericResult<Student[], Integer>> picList
        boxClass = (Class<?>) ((ParameterizedType) boxType).getRawType();
      } else if (boxType instanceof TypeVariable) { // List<T>
        GenericNode genericNode = classNode.getGenericNode(((TypeVariable<?>) boxType).getName());
        if (genericNode == null) {
          return;
        } else {
          boxClass = genericNode.getModelClass();
          childClassNode.setGenericNodes(genericNode.getChildGenericNode());
        }
      } else if (boxType instanceof Class) { // List<String>
        boxClass = (Class<?>) boxType;
      }
      if (boxClass == null) {
        return;
      }
      final String unifyFieldType = unifyType(boxClass.getSimpleName());
      if (unifyFieldType.equals(TYPE_MODEL)) {
        childClassNode.setList(true);
        childNodeClass = boxClass;
      } else {
        fieldNode.setType(unifyFieldType + "[]");
        return;
      }
    }
    if (childNodeClass == null) {
      childNodeClass = fieldClass;
      childGenericType = field.getGenericType();
      // V object
      if (childGenericType instanceof TypeVariable) {
        GenericNode genericNode =
            classNode.getGenericNode(((TypeVariable<?>) childGenericType).getName());
        if (genericNode == null) {
          return;
        } else {
          childNodeClass = genericNode.getModelClass();
          childClassNode.setGenericNodes(genericNode.getChildGenericNode());
        }
      } else if (childGenericType instanceof ParameterizedType) {
        Type[] paramTypes = ((ParameterizedType) childGenericType).getActualTypeArguments();
        for (Type paramType : paramTypes) {
          if (paramType instanceof TypeVariable) {
            GenericNode genericNode =
                classNode.getGenericNode(((TypeVariable<?>) paramType).getName());
            if (genericNode != null) {
              childClassNode.addGenericNode(genericNode);
            }
          }
        }
      }
      final String unifyFieldType = unifyType(childNodeClass.getSimpleName());
      if (!unifyFieldType.equals(TYPE_MODEL)) {
        fieldNode.setType(unifyFieldType);
        return;
      }
    }
    // 解析泛型
    parseGenericNodesInType(childNodeClass, childGenericType, childClassNode.getGenericNodes());
    childClassNode.setClassName(childNodeClass.getName());
    childClassNode.setModelClass(childNodeClass);
    fieldNode.setChildNode(childClassNode);

    if (!inClassDependencyTree(fieldNode, classNode)) {
      parseClassNodeByReflection(childClassNode);
    }
  }
}
