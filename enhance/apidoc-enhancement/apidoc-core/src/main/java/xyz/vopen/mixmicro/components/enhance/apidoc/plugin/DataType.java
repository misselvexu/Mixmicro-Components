package xyz.vopen.mixmicro.components.enhance.apidoc.plugin;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
class DataType {

  private DataType() {}

  public static final String STRING = "string";
  public static final String NUMBER = "number";
  public static final String BOOLEAN = "boolean";
  public static final String DOUBLE = "double";
  public static final String FLOAT = "float";
  public static final String OBJECT = "object";
  public static final String CHAR = "char";
  public static final String CHARACTER = "Character";
  public static final String DATE = "date";
  public static final String ARRAY = "array";
  public static final String ARRAY_NUMBER = "array<number>";
  public static final String ARRAY_STRING = "array<string>";
  public static final String ARRAY_BOOLEAN = "array<boolean>";
  public static final String ARRAY_OBJECT = "array<object>";
  public static final String MOCK = "@mock=";

  /**
   * get rap type of param node
   *
   * @param nodeType node param type
   * @return type of param
   */
  public static String rapTypeOfNode(String nodeType) {
    if (nodeType == null || nodeType.length() == 0) {
      return STRING;
    }
    if (isBooleanType(nodeType)) {
      return BOOLEAN;
    }
    if (isStringType(nodeType)) {
      return STRING;
    }

    if (isNumberType(nodeType)) {
      return NUMBER;
    }

    if (nodeType.endsWith("[]")) {
      String cType = nodeType.replace("[]", "");

      if (isBooleanType(cType)) {
        return ARRAY_BOOLEAN;
      }

      if (isStringType(cType)) {
        return ARRAY_STRING;
      }

      if (isNumberType(cType)) {
        return ARRAY_NUMBER;
      }

      return ARRAY_OBJECT;

    } else {
      return OBJECT;
    }
  }

  /**
   * is nodeType string an array or not
   *
   * @param nodeType node param type
   * @return boolean type
   */
  public static boolean isArrayType(String nodeType) {
    return nodeType != null && nodeType.endsWith("[]");
  }

  /**
   * get mock type of param node
   *
   * @param nodeType node param type
   * @return mock string
   */
  public static String mockTypeOfNode(String nodeType) {
    if (isArrayType(nodeType)) {
      nodeType = nodeType.replace("[]", "");
    }

    if (isBooleanType(nodeType)) {
      return MOCK + "@boolean".toUpperCase();
    } else if (isFloatType(nodeType)) {
      return MOCK + "@float".toUpperCase();
    } else if (isIntType(nodeType)) {
      return MOCK + "@integer".toUpperCase();
    } else if (isCharType(nodeType)) {
      return MOCK + "@character".toUpperCase();
    } else if ("date".equalsIgnoreCase(nodeType)) {
      return MOCK + "@datetime".toUpperCase();
    } else if (STRING.equalsIgnoreCase(nodeType)) {
      return MOCK + "@string".toUpperCase();
    } else {
      return "";
    }
  }

  /**
   * return mock value
   *
   * @param value param value
   * @return mock value
   */
  public static String mockValue(Object value) {
    return MOCK + value;
  }

  private static boolean isBooleanType(String pType) {
    return pType != null && pType.equalsIgnoreCase(BOOLEAN);
  }

  private static boolean isNumberType(String pType) {
    return isFloatType(pType) || isIntType(pType);
  }

  private static boolean isFloatType(String pType) {
    return pType != null && (pType.equalsIgnoreCase(FLOAT) || pType.equalsIgnoreCase(DOUBLE));
  }

  private static boolean isIntType(String pType) {
    return pType != null
        && (pType.equalsIgnoreCase("int")
            || pType.equalsIgnoreCase("byte")
            || pType.equalsIgnoreCase("short")
            || pType.equalsIgnoreCase("long"));
  }

  private static boolean isStringType(String pType) {
    return pType != null && (pType.equalsIgnoreCase(DATE) || pType.equalsIgnoreCase(STRING));
  }

  private static boolean isCharType(String pType) {
    return (CHAR.equalsIgnoreCase(pType) || CHARACTER.equalsIgnoreCase(pType));
  }
}
