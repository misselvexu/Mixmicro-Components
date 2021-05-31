package xyz.vopen.mixmicro.components.enhance.apidoc;

import xyz.vopen.mixmicro.components.enhance.apidoc.genetator.HtmlDocGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.plugin.RapSupportPlugin;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CacheUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * main entrance
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class Docs {

  private static final Logger LOGGER = Logger.getLogger(Docs.class.getName());

  private static final String CONFIG_FILE = "docs.config";

  public static void main(String[] args) {
    DocsConfig config = loadProps();
    buildHtmlDocs(config);
  }

  /** build html api docs */
  public static void buildHtmlDocs(DocsConfig config) {
    DocContext.init(config);
    HtmlDocGenerator docGenerator = new HtmlDocGenerator();
    List<ControllerNode> controllerNodeList = docGenerator.getControllerNodeList();
    if (null == controllerNodeList || controllerNodeList.isEmpty()) {
      LOGGER.warning("cant find any controller or endpoint class");
      return;
    }
    if (!config.getIgnorePackages().isEmpty()) {
      controllerNodeList =
          controllerNodeList.stream()
              .filter(
                  controllerNode ->
                      !config.getIgnorePackages().contains(controllerNode.getPackageName()))
              .collect(Collectors.toList());
    }
    DocContext.setControllerNodeList(controllerNodeList);
    docGenerator.generateDocs();
    CacheUtils.saveControllerNodes(controllerNodeList);
    DocsConfig docsConfig = DocContext.getDocsConfig();
    if (docsConfig.getRapProjectId() != null && docsConfig.getRapHost() != null) {
      IPluginSupport rapPlugin = new RapSupportPlugin();
      rapPlugin.execute(controllerNodeList);
    }
    for (IPluginSupport plugin : config.getPlugins()) {
      plugin.execute(controllerNodeList);
    }
  }

  /**
   * wrap response into a common structure,don't forget to put responseNode into map.
   *
   * <p>default is:
   *
   * <p>{ code : 0, data: ${response} msg: 'success' }
   *
   * @param responseWrapper response wrapper
   */
  public static void setResponseWrapper(IResponseWrapper responseWrapper) {
    DocContext.setResponseWrapper(responseWrapper);
  }

  private static DocsConfig loadProps() {
    try {
      Properties properties = new Properties();
      properties.load(new FileReader(CONFIG_FILE));
      DocsConfig config = new DocsConfig();
      config.projectPath = properties.getProperty("projectPath", null);

      if (config.projectPath == null) {
        throw new RuntimeException("projectPath property is needed in the config file.");
      }

      config.docsPath = properties.getProperty("docsPath", null);
      config.resourcePath = properties.getProperty("resourcePath", null);
      config.mvcFramework = properties.getProperty("mvcFramework", "");
      return config;
    } catch (IOException e) {
      e.printStackTrace();

      try {
        File configFile = new File(CONFIG_FILE);
        configFile.createNewFile();
      } catch (Exception ex) {
        e.printStackTrace();
      }

      throw new RuntimeException("you need to set projectPath property in " + CONFIG_FILE);
    }
  }
}
