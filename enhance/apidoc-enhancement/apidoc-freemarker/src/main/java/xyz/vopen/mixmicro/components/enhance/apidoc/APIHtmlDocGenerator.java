package xyz.vopen.mixmicro.components.enhance.apidoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.apidoc.generator.APIGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.genetator.HtmlDocGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.plugin.RapSupportPlugin;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CacheUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * main entrance
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class APIHtmlDocGenerator implements APIGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(APIHtmlDocGenerator.class);

  public APIHtmlDocGenerator(IResponseWrapper responseWrapper) {
    DocContext.setResponseWrapper(responseWrapper);
  }

  @Override
  public void generate(DocsConfig config) {
    DocContext.initForHTMLGenerator(config);
    HtmlDocGenerator docGenerator = new HtmlDocGenerator();
    List<ControllerNode> controllerNodeList = docGenerator.getControllerNodeList();
    if (null == controllerNodeList || controllerNodeList.isEmpty()) {
      LOGGER.warn("cant find any controller or endpoint class");
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
}
