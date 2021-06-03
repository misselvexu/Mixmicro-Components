package xyz.vopen.mixmicro.components.enhance.apidoc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import xyz.vopen.mixmicro.components.enhance.apidoc.consts.BuildToolType;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class CommonUtils {

  private CommonUtils() {}

  private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

  /**
   * write content to file
   *
   * @param f file
   * @param content file content
   * @throws IOException IO exception
   */
  public static void writeToDisk(File f, String content) throws IOException {
    mkdirsForFile(f);
    try (BufferedWriter writer =
        new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
      writer.write(content);
    } catch (Exception e) {
      LOGGER.error("write to disk error", e);
    }
  }

  /**
   * close stream
   *
   * @param stream Closeable
   */
  public static void closeSilently(Closeable stream) {
    if (stream != null) {
      try {
        stream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * simple read stream to String
   *
   * @param in InputStream
   * @return stream content string
   * @throws IOException IO exception
   */
  public static String streamToString(InputStream in) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
    char[] buffer = new char[4096];
    int bytesRead = -1;
    while ((bytesRead = reader.read(buffer)) != -1) {
      stringBuilder.append(buffer, 0, bytesRead);
    }
    reader.close();
    return stringBuilder.toString();
  }

  /**
   * some parse url may has double quotation, remove them
   *
   * @param rawUrl raw url
   * @return url
   */
  public static String removeQuotations(String rawUrl) {
    return rawUrl.replace("\"", "").trim();
  }

  /**
   * remove some characters like [* \n]
   *
   * @param content content
   * @return cleaned content
   */
  public static String cleanCommentContent(String content) {
    return content.replace("*", "").replace("\n", "").trim();
  }

  /**
   * get url with base url
   *
   * @param baseUrl base url
   * @param relativeUrl sub url
   * @return action url
   */
  public static String getActionUrl(String baseUrl, String relativeUrl) {

    if (relativeUrl == null) {
      return "";
    }

    if (baseUrl == null) {
      return relativeUrl;
    }

    if (baseUrl.endsWith("/")) {
      baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
    }

    if (!relativeUrl.startsWith("/")) {
      relativeUrl = "/" + relativeUrl;
    }

    return baseUrl + relativeUrl;
  }

  /**
   * make first word lower case
   *
   * @param name content
   * @return lower case value
   */
  public static String lowercase(String name) {
    if (name != null && name.length() != 0) {
      if (name.length() > 1
          && Character.isUpperCase(name.charAt(1))
          && Character.isUpperCase(name.charAt(0))) {
        return name;
      } else {
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
      }
    } else {
      return name;
    }
  }

  /**
   * make first word capitalize
   *
   * @param name english word
   * @return capitalize name
   */
  public static String capitalize(String name) {
    if (name != null && name.length() != 0) {
      char[] chars = name.toCharArray();
      chars[0] = Character.toUpperCase(chars[0]);
      return new String(chars);
    } else {
      return name;
    }
  }

  /**
   * join string array , （ e.g. ([a,a,a] , .) = a.a.a )
   *
   * @param array origin array
   * @param separator separator
   * @return join string
   */
  public static String joinArrayString(String[] array, String separator) {
    if (array == null || array.length == 0) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (int i = 0, len = array.length; i != len; i++) {
      builder.append(array[i]);
      if (i != len - 1) {
        builder.append(separator);
      }
    }
    return builder.toString();
  }

  /**
   * get file name without extension
   *
   * @param javaFile java file
   * @return file name
   */
  public static String getJavaFileName(File javaFile) {
    String fileName = javaFile.getName();
    return fileName.substring(0, fileName.lastIndexOf("."));
  }

  /**
   * search files match filter, store in result
   *
   * @param rootPath project root path
   * @param filter file name filter
   * @param result file filter result
   * @param stopAtFirstResult stop when first file matches
   */
  public static void wideSearchFile(
      File rootPath, FilenameFilter filter, List<File> result, boolean stopAtFirstResult) {
    File[] fileList = rootPath.listFiles();
    List<File> dirPaths = new ArrayList<>();
    if (null == fileList || fileList.length == 0) {
      LOGGER.error("There is not any file in root path: {}", rootPath.getName());
      return;
    }

    for (File f : fileList) {
      if (f.isFile() && filter.accept(f, f.getName())) {
        result.add(f);
        if (stopAtFirstResult) {
          return;
        }
      } else if (f.isDirectory()) {
        dirPaths.add(f);
      }
    }

    for (File dir : dirPaths) {
      if (stopAtFirstResult && !result.isEmpty()) {
        return;
      }
      wideSearchFile(dir, filter, result, stopAtFirstResult);
    }
  }

  /**
   * judge dir is in file's path or not
   *
   * @param f source file
   * @param stopPath stopPath
   * @param dirName folder name
   * @return file has folder or not
   */
  public static boolean hasDirInFile(File f, File stopPath, String dirName) {
    File p = f.getParentFile();
    while ((stopPath == null && p != null)
        || (stopPath != null && !p.getAbsolutePath().equals(stopPath.getAbsolutePath()))) {
      if (dirName.equals(p.getName())) {
        return true;
      }
      p = p.getParentFile();
    }
    return false;
  }

  /**
   * the project is a play framework or not
   *
   * @param projectDir 工程目录
   * @return is play framework
   */
  public static boolean isPlayFramework(File projectDir) {
    File ymlFile = new File(projectDir, "conf/dependencies.yml");
    if (!ymlFile.exists()) {
      return false;
    }
    File routesFile = new File(projectDir, "conf/routes");
    return !routesFile.exists();
  }

  /**
   * the project is a spring mvc framework or not
   *
   * @param javaSrcDir java file folder
   * @return is spring framework
   */
  public static boolean isSpringFramework(File javaSrcDir) {
    List<File> result = new ArrayList<>();
    CommonUtils.wideSearchFile(
        javaSrcDir,
        (f, name) ->
            name.endsWith(".java")
                && ParseUtils.compilationUnit(f).getImports().stream()
                    .anyMatch(im -> im.getNameAsString().contains("org.springframework.web")),
        result,
        true);
    return !result.isEmpty();
  }

  /**
   * the project is a JFinal framework or not
   *
   * @param javaSrcDir java file folder
   * @return js JFinal framework
   */
  public static boolean isJFinalFramework(File javaSrcDir) {
    List<File> result = new ArrayList<>();
    CommonUtils.wideSearchFile(
        javaSrcDir,
        (f, name) ->
            name.endsWith(".java")
                && ParseUtils.compilationUnit(f).getImports().stream()
                    .anyMatch(im -> im.getNameAsString().contains("com.jfinal.core")),
        result,
        true);
    return !result.isEmpty();
  }

  /**
   * is value type or not
   *
   * @param value value
   * @return value type
   */
  public static boolean isValueType(Object value) {
    return value instanceof Number || value instanceof String || value instanceof java.util.Date;
  }

  /**
   * get simple class name
   *
   * @param packageClass package reference
   * @return class name
   */
  public static String getClassName(String packageClass) {
    String[] parts = packageClass.split("\\.");
    return parts[parts.length - 1];
  }

  /**
   * get project build tool type
   *
   * @param projectDir project folder
   * @return BuildToolType
   */
  private static BuildToolType getProjectBuildTool(File projectDir) {
    if (new File(projectDir, "settings.gradle").exists()) {
      return BuildToolType.GRADLE;
    }

    if (new File(projectDir, "pom.xml").exists()) {
      return BuildToolType.MAVEN;
    }

    return BuildToolType.UNKOWN;
  }

  /**
   * get project modules name
   *
   * @param projectDir project folder path
   * @return module names
   */
  public static List<String> getModuleNames(File projectDir) {
    BuildToolType buildToolType = getProjectBuildTool(projectDir);

    List<String> moduleNames = new ArrayList<>();

    // gradle
    if (buildToolType == BuildToolType.GRADLE) {
      try (BufferedReader settingReader =
          new BufferedReader(
              new InputStreamReader(
                  new FileInputStream(new File(projectDir, "settings.gradle"))))) {
        String lineText;
        String keyword = "include ";
        while ((lineText = settingReader.readLine()) != null) {
          int inIndex = lineText.indexOf(keyword);
          if (inIndex != -1) {
            moduleNames.add(
                lineText.substring(inIndex + keyword.length()).replace("'", "").replace("\"", ""));
          }
        }
        CommonUtils.closeSilently(settingReader);
      } catch (IOException ex) {
        LOGGER.error("read setting.gradle error", ex);
      }
    }

    // maven
    if (buildToolType == BuildToolType.MAVEN) {
      try {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        saxParser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // Compliant

        saxParser.parse(
            new File(projectDir, "pom.xml"),
            new DefaultHandler() {

              String moduleName;
              boolean isModuleTag;

              @Override
              public void startElement(
                  String uri, String localName, String qName, Attributes attributes) {
                if ("module".equalsIgnoreCase(qName)) {
                  isModuleTag = true;
                }
              }

              @Override
              public void endElement(String uri, String localName, String qName) {
                if ("module".equalsIgnoreCase(qName)) {
                  moduleNames.add(moduleName);
                  isModuleTag = false;
                }
              }

              @Override
              public void characters(char[] ch, int start, int length) {
                if (isModuleTag) {
                  moduleName = new String(ch, start, length);
                }
              }
            });
      } catch (Exception ex) {
        LOGGER.error("read pom.xml error", ex);
      }
    }

    if (!moduleNames.isEmpty()) {
      LOGGER.info("find multi modules in this project:{}", moduleNames);
    }

    return moduleNames;
  }

  /**
   * create dirs for file
   *
   * @param file dist file
   */
  public static void mkdirsForFile(File file) {
    if (file.isFile() && !file.getParentFile().exists()) {
      boolean mkdirs = file.getParentFile().mkdirs();
      if (mkdirs) {
        LOGGER.error("create new folder error");
      }
    }
  }
}
