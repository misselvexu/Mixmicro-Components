package xyz.vopen.mixmicro.components.enhance.apidoc;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class ResourcesUtils {

  private ResourcesUtils() {}

  private static boolean isDebug = false;
  private static String debugResourcePath;

  /**
   * get template file
   *
   * @param fileName template file name
   * @return input stream
   */
  public static InputStream getTemplateFile(String fileName) throws FileNotFoundException {

    if (isDebug) {
      return new FileInputStream(new File(debugResourcePath, fileName));
    }

    final String userResPath = getUserResourcePath();

    if (getUserResourcePath() != null) {
      File tplFile = new File(userResPath, fileName);
      if (tplFile.isFile() && tplFile.exists()) {
        return new FileInputStream(tplFile);
      }
    }

    return ResourcesUtils.class.getResourceAsStream("/" + fileName);
  }

  /**
   * get code template file
   *
   * @param fileName file name
   * @return input stream
   */
  public static InputStream getCodeTemplateFile(String fileName) throws FileNotFoundException {
    return getTemplateFile(fileName);
  }

  /**
   * get freemarker template
   *
   * @param fileName file name
   * @return Template
   * @throws IOException exception
   */
  public static Template getFreemarkerTemplate(String fileName) throws IOException {
    Configuration conf = new Configuration(Configuration.VERSION_2_3_0);
    conf.setDefaultEncoding(StandardCharsets.UTF_8.name());
    if (isDebug) {
      conf.setDirectoryForTemplateLoading(new File(debugResourcePath));
    } else {
      final String userResPath = getUserResourcePath();
      File tplFile = new File(userResPath, fileName);
      if (tplFile.isFile() && tplFile.exists()) {
        conf.setDirectoryForTemplateLoading(new File(userResPath));
      } else {
        conf.setClassForTemplateLoading(ResourcesUtils.class, "/");
      }
    }
    return conf.getTemplate(fileName);
  }

  private static String getUserResourcePath() {
    return DocContext.getDocsConfig().resourcePath;
  }

  public static void setDebug() {
    isDebug = true;
    debugResourcePath = System.getProperty("user.dir") + "/build/resources/main";
  }
}
