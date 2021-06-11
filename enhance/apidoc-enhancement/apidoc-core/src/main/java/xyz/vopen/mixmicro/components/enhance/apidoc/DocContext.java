package xyz.vopen.mixmicro.components.enhance.apidoc;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.apidoc.annotations.Ignore;
import xyz.vopen.mixmicro.components.enhance.apidoc.consts.ProjectType;
import xyz.vopen.mixmicro.components.enhance.apidoc.exception.ConfigException;
import xyz.vopen.mixmicro.components.enhance.apidoc.exception.FileParseException;
import xyz.vopen.mixmicro.components.enhance.apidoc.i18n.I18n;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.parser.*;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CacheUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.ParseUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * to judge project which framework is using and make some initialization
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class DocContext {

  private DocContext() {}

  private static final String JAVA_FILE_SUFFIX = ".java";
  private static final String DIR_END = "/";

  private static final Logger LOGGER = LoggerFactory.getLogger(DocContext.class);

  private static String projectPath;
  private static String docPath;
  // multi modules
  private static final List<String> javaSrcPaths = new ArrayList<>();
  private static AbsControllerParser controllerParser;
  private static List<File> controllerFiles;
  private static IResponseWrapper responseWrapper;
  private static DocsConfig config;
  private static I18n i18n;

  private static String currentApiVersion;
  private static final List<String> apiVersionList = new ArrayList<>();
  private static List<ControllerNode> lastVersionControllerNodes;
  private static List<ControllerNode> controllerNodeList;

  /**
   * initialize for html generator
   *
   * @param config DocsConfig
   */
  public static void initForHTMLGenerator(DocsConfig config) {
    if (config.projectPath == null || !new File(config.projectPath).exists()) {
      throw new ConfigException(String.format("projectDir doesn't exists. %s", projectPath));
    }

    if (config.getApiVersion() == null) {
      throw new ConfigException("api version cannot be null");
    }
    if (config.getProjectName() == null) {
      config.setProjectName("Mixmicro API Docs");
    }
    DocContext.config = config;
    if (null != config.getLocale()) {
      i18n = new I18n(config.getLocale());
    }
    DocContext.currentApiVersion = config.getApiVersion();
    setProjectPath(config.projectPath);
    setDocPath(config);
    initApiVersions();
    File logFile = getLogFile();
    if (logFile.exists()) {
      try {
        Files.delete(logFile.toPath());
      } catch (IOException e) {
        LOGGER.error("{} delete error", logFile.getName(), e);
      }
    }
    if (config.getJavaSrcPaths().isEmpty()) {
      findOutJavaSrcPaths();
    } else {
      javaSrcPaths.addAll(config.getJavaSrcPaths());
    }
    LOGGER.info("find java src paths:{}", javaSrcPaths);
    ProjectType projectType = findOutProjectType();
    findOutControllers(projectType);
    initLastVersionControllerNodes();
  }

  /**
   * initialize for data generator
   *
   * @param config DocsConfig
   */
  public static void initForDataGenerator(DocsConfig config) {
    if (config.projectPath == null || !new File(config.projectPath).exists()) {
      throw new ConfigException(String.format("projectDir doesn't exists. %s", projectPath));
    }

    if (config.getApiVersion() == null) {
      throw new ConfigException("api version cannot be null");
    }
    if (config.getProjectName() == null) {
      config.setProjectName("Mixmicro API Docs");
    }
    DocContext.config = config;
    if (null != config.getLocale()) {
      i18n = new I18n(config.getLocale());
    }
    DocContext.currentApiVersion = config.getApiVersion();
    setProjectPath(config.projectPath);
    if (config.getJavaSrcPaths().isEmpty()) {
      findOutJavaSrcPaths();
    } else {
      javaSrcPaths.addAll(config.getJavaSrcPaths());
    }
    LOGGER.info("find java src paths:{}", javaSrcPaths);
    findOutControllers(findOutProjectType());
  }

  private static void initLastVersionControllerNodes() {
    File docDir = new File(docPath).getParentFile();
    File[] childDirs = docDir.listFiles();
    if (childDirs != null && childDirs.length != 0) {
      File lastVerDocDir = null;
      for (File childDir : childDirs) {
        if (childDir.isDirectory()
            && !currentApiVersion.equals(childDir.getName())
            && (lastVerDocDir == null || childDir.lastModified() > lastVerDocDir.lastModified())) {
          lastVerDocDir = childDir;
        }
      }
      if (lastVerDocDir != null) {
        lastVersionControllerNodes = CacheUtils.getControllerNodes(lastVerDocDir.getName());
      }
    }
  }

  private static void findOutJavaSrcPaths() {
    // try to find javaSrcPaths
    File projectDir = new File(projectPath);

    // module name maybe:
    // include 'auth:auth-redis'
    List<String> moduleNames = CommonUtils.getModuleNames(projectDir);
    if (!moduleNames.isEmpty()) {
      for (String moduleName : moduleNames) {
        final String moduleRelativePath = moduleName.replace(":", "/");
        String javaSrcPath = findModuleSrcPath(new File(projectDir, moduleRelativePath));
        javaSrcPaths.add(javaSrcPath);
      }
    }

    // is it a simple java project?
    if (javaSrcPaths.isEmpty()) {
      String javaSrcPath = findModuleSrcPath(projectDir);
      javaSrcPaths.add(javaSrcPath);
    }
  }

  /** 获取文档目录下所有api版本 */
  private static void initApiVersions() {
    File docDir = new File(docPath).getParentFile();
    String[] diffVersionApiDirs =
        docDir.list((dir, name) -> dir.isDirectory() && !name.startsWith("."));
    if (diffVersionApiDirs != null) {
      Collections.addAll(DocContext.apiVersionList, diffVersionApiDirs);
    }
  }

  private static ProjectType findOutProjectType() {
    // which mvc framework
    ProjectType projectType = null;

    if (config.isSpringMvcProject()) {
      projectType = ProjectType.SPRING;
    } else if (config.isJFinalProject()) {
      projectType = ProjectType.JFINAL;
    } else if (config.isPlayProject()) {
      projectType = ProjectType.PLAY;
    } else if (config.isGeneric()) {
      projectType = ProjectType.GENERIC;
    }

    if (projectType == null) {
      LOGGER.info("project type not set, try to figure out...");
      for (String javaSrcPath : javaSrcPaths) {
        File javaSrcDir = new File(javaSrcPath);
        if (CommonUtils.isSpringFramework(javaSrcDir)) {
          projectType = ProjectType.SPRING;
        } else if (CommonUtils.isPlayFramework(new File(getProjectPath()))) {
          projectType = ProjectType.PLAY;
        } else if (CommonUtils.isJFinalFramework(javaSrcDir)) {
          projectType = ProjectType.JFINAL;
        }

        if (projectType != null) {
          return projectType;
        }
      }
    }

    projectType = projectType != null ? projectType : ProjectType.GENERIC;

    LOGGER.info("found it a:{} project, tell us if we are wrong.", projectType);

    return projectType;
  }

  private static void findOutControllers(ProjectType projectType) {
    controllerFiles = new ArrayList<>();
    Set<String> controllerFileNames;

    for (String javaSrcPath : getJavaSrcPaths()) {
      LOGGER.info("start find controllers in path : {}", javaSrcPath);
      File javaSrcDir = new File(javaSrcPath);
      List<File> result = new ArrayList<>();
      switch (projectType) {
        case PLAY:
          controllerParser = new PlayControllerParser();
          controllerFileNames = new LinkedHashSet<>();
          List<PlayRoutesParser.RouteNode> routeNodeList =
              PlayRoutesParser.INSTANCE.getRouteNodeList();

          for (PlayRoutesParser.RouteNode node : routeNodeList) {
            controllerFileNames.add(node.controllerFile);
          }

          for (String controllerFileName : controllerFileNames) {
            controllerFiles.add(new File(controllerFileName));
          }

          break;
        case JFINAL:
          controllerParser = new JFinalControllerParser();
          controllerFileNames = new LinkedHashSet<>();
          List<JFinalRoutesParser.RouteNode> jFinalRouteNodeList =
              JFinalRoutesParser.INSTANCE.getRouteNodeList();

          for (JFinalRoutesParser.RouteNode node : jFinalRouteNodeList) {
            controllerFileNames.add(node.controllerFile);
          }

          for (String controllerFileName : controllerFileNames) {
            controllerFiles.add(new File(controllerFileName));
          }
          break;
        case SPRING:
          controllerParser = new SpringControllerParser();
          CommonUtils.wideSearchFile(
              javaSrcDir,
              (f, name) ->
                  f.getName().endsWith(JAVA_FILE_SUFFIX)
                      && ParseUtils.compilationUnit(f)
                          .findAll(ClassOrInterfaceDeclaration.class)
                          .stream()
                          .anyMatch(
                              cd ->
                                  (cd.getAnnotationByName("Controller").isPresent()
                                          || cd.getAnnotationByName("RestController").isPresent())
                                      && !cd.getAnnotationByName(Ignore.class.getSimpleName())
                                          .isPresent()),
              result,
              false);
          controllerFiles.addAll(result);
          break;
        default:
          controllerParser = new GenericControllerParser();
          CommonUtils.wideSearchFile(
              javaSrcDir,
              (f, name) ->
                  f.getName().endsWith(JAVA_FILE_SUFFIX)
                      && ParseUtils.compilationUnit(f)
                          .findAll(ClassOrInterfaceDeclaration.class)
                          .stream()
                          .anyMatch(
                              cd ->
                                  cd.findAll(MethodDeclaration.class).stream()
                                      .anyMatch(
                                          md -> md.getAnnotationByName("ApiDoc").isPresent())),
              result,
              false);
          controllerFiles.addAll(result);
          break;
      }
      for (File controllerFile : result) {
        LOGGER.info("find controller file:{}", controllerFile.getName());
      }
    }
  }

  private static String findModuleSrcPath(File moduleDir) {

    List<File> result = new ArrayList<>();
    CommonUtils.wideSearchFile(
        moduleDir,
        (file, name) -> {
          if (name.endsWith(JAVA_FILE_SUFFIX) && file.getAbsolutePath().contains("src")) {
            Optional<PackageDeclaration> opPackageDeclaration =
                ParseUtils.compilationUnit(file).getPackageDeclaration();
            if (opPackageDeclaration.isPresent()) {
              String packageName = opPackageDeclaration.get().getNameAsString();
              return !CommonUtils.hasDirInFile(file, moduleDir, "test")
                  || packageName.contains("test");
            }
            return !CommonUtils.hasDirInFile(file, moduleDir, "test");
          }
          return false;
        },
        result,
        true);

    if (result.isEmpty()) {
      throw new FileParseException(
          "cannot find any java file in this module : " + moduleDir.getName());
    }

    File oneJavaFile = result.get(0);
    Optional<PackageDeclaration> opPackageDeclaration =
        ParseUtils.compilationUnit(oneJavaFile).getPackageDeclaration();
    String parentPath = oneJavaFile.getParentFile().getAbsolutePath();
    return opPackageDeclaration
        .map(
            packageDeclaration ->
                parentPath.substring(
                    0, parentPath.length() - packageDeclaration.getNameAsString().length()))
        .orElseGet(() -> parentPath + "/");
  }

  /**
   * get log file path
   *
   * @return log file
   */
  public static File getLogFile() {
    return new File(DocContext.getDocPath(), "apidoc.log");
  }

  /** get project path */
  public static String getProjectPath() {
    return projectPath;
  }

  private static void setProjectPath(String projectPath) {
    if (projectPath != null) {
      DocContext.projectPath = new File(projectPath).getAbsolutePath() + DIR_END;
    }
  }

  /**
   * api docs output path
   *
   * @return doc path
   */
  public static String getDocPath() {
    return docPath;
  }

  private static void setDocPath(DocsConfig config) {
    if (StringUtils.isBlank(config.docsPath)) {
      config.docsPath = projectPath + "apidocs";
    }

    File docDir = new File(config.docsPath, config.apiVersion);
    if (!docDir.exists()) {
      boolean mkdirs = docDir.mkdirs();
      if (!mkdirs) {
        LOGGER.error("create folder:{} failed", config.docsPath);
      }
    }
    DocContext.docPath = docDir.getAbsolutePath();
  }

  /**
   * get java src paths
   *
   * @return path list
   */
  public static List<String> getJavaSrcPaths() {
    return javaSrcPaths;
  }

  /**
   * get all controllers in this project
   *
   * @return controller file list
   */
  public static File[] getControllerFiles() {
    return controllerFiles.toArray(new File[0]);
  }

  /**
   * get controller parser, it will return different parser by different framework you are using.
   *
   * @return abstract controller parser
   */
  public static AbsControllerParser controllerParser() {
    return controllerParser;
  }

  public static IResponseWrapper getResponseWrapper() {
    if (responseWrapper == null) {
      responseWrapper =
          responseNode -> {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 0);
            resultMap.put("data", responseNode);
            resultMap.put("msg", "success");
            return resultMap;
          };
    }
    return responseWrapper;
  }

  public static List<ControllerNode> getControllerNodeList() {
    return controllerNodeList;
  }

  public static void setControllerNodeList(List<ControllerNode> controllerNodeList) {
    DocContext.controllerNodeList = controllerNodeList;
  }

  public static DocsConfig getDocsConfig() {
    return DocContext.config;
  }

  public static String getCurrentApiVersion() {
    return currentApiVersion;
  }

  public static List<String> getApiVersionList() {
    return apiVersionList;
  }

  public static List<ControllerNode> getLastVersionControllerNodes() {
    return lastVersionControllerNodes;
  }

  public static I18n getI18n() {
    return i18n;
  }

  static void setResponseWrapper(IResponseWrapper responseWrapper) {
    DocContext.responseWrapper = responseWrapper;
  }
}
