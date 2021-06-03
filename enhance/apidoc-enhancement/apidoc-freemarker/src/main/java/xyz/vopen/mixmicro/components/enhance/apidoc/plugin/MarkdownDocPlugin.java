package xyz.vopen.mixmicro.components.enhance.apidoc.plugin;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.IPluginSupport;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.ResourcesUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * export doc as markdown plugin
 *
 * @author tino.tang
 */
public class MarkdownDocPlugin implements IPluginSupport {

  private static final Logger LOGGER = LoggerFactory.getLogger(MarkdownDocPlugin.class);

  @Override
  public void execute(List<ControllerNode> controllerNodeList) {
    final Template ctrlTemplate = getDocTpl();
    if (null == ctrlTemplate) {
      LOGGER.error("get template file null");
      return;
    }
    final String docFileName =
        String.format(
            "%s-%s-api-docs.md",
            DocContext.getDocsConfig().getProjectName(),
            DocContext.getDocsConfig().getApiVersion());
    final File docFile = new File(DocContext.getDocPath(), docFileName);

    try (FileWriter docFileWriter = new FileWriter(docFile)) {
      Map<String, Object> data = new HashMap<>();
      data.put("controllerNodes", controllerNodeList);
      data.put("currentApiVersion", DocContext.getCurrentApiVersion());
      data.put("projectName", DocContext.getDocsConfig().getProjectName());
      data.put("i18n", DocContext.getI18n());
      ctrlTemplate.process(data, docFileWriter);
    } catch (TemplateException | IOException ex) {
      LOGGER.error("generate markdown error", ex);
    }
  }

  /**
   * get doc template
   *
   * @return template
   */
  private Template getDocTpl() {
    try {
      return ResourcesUtils.getFreemarkerTemplate("api-doc.md.ftl");
    } catch (IOException e) {
      LOGGER.error("get template file api-doc.md.ftl error");
    }
    return null;
  }
}
