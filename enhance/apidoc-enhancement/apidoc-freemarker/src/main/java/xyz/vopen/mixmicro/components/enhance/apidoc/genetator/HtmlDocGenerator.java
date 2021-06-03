package xyz.vopen.mixmicro.components.enhance.apidoc.genetator;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.ResourcesUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.builder.HtmlControllerDocBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.generator.AbsDocGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Html Api docs generator
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class HtmlDocGenerator extends AbsDocGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(HtmlDocGenerator.class);

  public HtmlDocGenerator() {
    super(DocContext.controllerParser(), new HtmlControllerDocBuilder());
  }

  @Override
  public void generateIndex(List<ControllerNode> controllerNodeList) {
    FileWriter docFileWriter = null;
    try {
      LOGGER.info("generate index start !!!");
      final Template ctrlTemplate = getIndexTpl();
      final File docFile = new File(DocContext.getDocPath(), "index.html");
      docFileWriter = new FileWriter(docFile);
      Map<String, Object> data = new HashMap<>();
      data.put("controllerNodeList", controllerNodeList);
      data.put("currentApiVersion", DocContext.getCurrentApiVersion());
      data.put("apiVersionList", DocContext.getApiVersionList());
      data.put("projectName", DocContext.getDocsConfig().getProjectName());
      data.put("i18n", DocContext.getI18n());
      ctrlTemplate.process(data, docFileWriter);
      LOGGER.info("generate index done !!!");
    } catch (TemplateException | IOException ex) {
      LOGGER.error("generate index fail !!!", ex);
    } finally {
      CommonUtils.closeSilently(docFileWriter);
    }
    copyCssStyle();
  }

  private void copyCssStyle() {
    try {
      String cssFileName = "style.css";
      File cssFile = new File(DocContext.getDocPath(), cssFileName);
      CommonUtils.writeToDisk(
          cssFile, CommonUtils.streamToString(ResourcesUtils.getTemplateFile(cssFileName)));
    } catch (IOException e) {
      LOGGER.error("copyCssStyle fail", e);
    }
  }

  private Template getIndexTpl() throws IOException {
    return ResourcesUtils.getFreemarkerTemplate("api-index.html.ftl");
  }
}
