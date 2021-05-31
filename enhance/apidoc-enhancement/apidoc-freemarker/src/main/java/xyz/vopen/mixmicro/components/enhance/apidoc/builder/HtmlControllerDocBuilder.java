package xyz.vopen.mixmicro.components.enhance.apidoc.builder;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.ResourcesUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.genetator.IOSModelCodeGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.genetator.JavaCodeGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.RequestNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * build html api docs for a controller
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class HtmlControllerDocBuilder implements IControllerDocBuilder {

  @Override
  public String buildDoc(ControllerNode controllerNode) throws IOException {

    for (RequestNode requestNode : controllerNode.getRequestNodes()) {
      if (requestNode.getResponseNode() != null
          && !requestNode.getResponseNode().getChildNodes().isEmpty()) {
        JavaCodeGenerator javaCodeGenerator = new JavaCodeGenerator(requestNode.getResponseNode());
        final String javaSrcUrl = javaCodeGenerator.generateCode();
        requestNode.setAndroidCodePath(javaSrcUrl);
        IOSModelCodeGenerator iosCodeGenerator = new IOSModelCodeGenerator(requestNode.getResponseNode());
        final String iosSrcUrl = iosCodeGenerator.generateCode();
        requestNode.setIosCodePath(iosSrcUrl);
      }
    }

    final Template ctrlTemplate = getControllerTpl();
    final File docFile = new File(DocContext.getDocPath(), controllerNode.getDocFileName());
    FileWriter docFileWriter = new FileWriter(docFile);
    Map<String, Object> data = new HashMap<>();
    data.put("controllerNodeList", DocContext.getControllerNodeList());
    data.put("controller", controllerNode);
    data.put("currentApiVersion", DocContext.getCurrentApiVersion());
    data.put("apiVersionList", DocContext.getApiVersionList());
    data.put("projectName", DocContext.getDocsConfig().getProjectName());
    data.put("i18n", DocContext.getI18n());

    try {
      ctrlTemplate.process(data, docFileWriter);
    } catch (TemplateException ex) {
      ex.printStackTrace();
    } finally {
      CommonUtils.closeSilently(docFileWriter);
    }
    return CommonUtils.streamToString(new FileInputStream(docFile));
  }

  private Template getControllerTpl() throws IOException {
    return ResourcesUtils.getFreemarkerTemplate("api-controller.html.ftl");
  }
}
