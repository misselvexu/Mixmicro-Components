package xyz.vopen.mixmicro.components.enhance.apidoc.provider;

import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class DocFieldHelper {

  private DocFieldHelper() {}

  public static String getPrefFieldName(String originFieldName) {
    String[] names = originFieldName.split("_");
    if (names.length == 1) {
      return CommonUtils.lowercase(names[0]);
    }
    StringBuilder fieldNameBuilder = new StringBuilder();
    fieldNameBuilder.append(CommonUtils.lowercase(names[0]));
    for (int i = 1; i < names.length; i++) {
      fieldNameBuilder.append(CommonUtils.capitalize(names[i]));
    }
    return fieldNameBuilder.toString();
  }

  public static String getPrefFieldType(String fieldType) {

    if (fieldType == null) {
      return "unknow";
    }

    if (fieldType.equalsIgnoreCase("int") || fieldType.equalsIgnoreCase("integer")) {
      return "int";
    } else if (fieldType.equalsIgnoreCase("short")) {
      return "short";
    } else if (fieldType.equalsIgnoreCase("byte")) {
      return "byte";
    } else if (fieldType.equalsIgnoreCase("long")) {
      return "long";
    } else if (fieldType.equalsIgnoreCase("boolean") || fieldType.equalsIgnoreCase("bool")) {
      return "boolean";
    } else if (fieldType.equalsIgnoreCase("float")) {
      return "float";
    } else if (fieldType.equalsIgnoreCase("double")) {
      return "double";
    } else if (fieldType.equalsIgnoreCase("String") || fieldType.equalsIgnoreCase("Date")) {
      return "String";
    } else {
      return fieldType;
    }
  }

  public static String getIosFieldType(String type) {
    if (type.equals("byte")) {
      return "int";
    } else if (type.equals("int")) {
      return "NSInteger";
    } else if (type.equals("short")) {
      return "short";
    } else if (type.equals("long")) {
      return "long";
    } else if (type.equals("float")) {
      return "CGFloat";
    } else if (type.equals("double")) {
      return "double";
    } else if (type.equals("boolean")) {
      return "BOOL";
    } else if (type.equalsIgnoreCase("String")) {
      return "NSString";
    } else {
      return type;
    }
  }
}
