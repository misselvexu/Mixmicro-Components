package xyz.vopen.mixmicro.components.enhance.apidoc.plugin;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.IPluginSupport;
import xyz.vopen.mixmicro.components.enhance.apidoc.ResourcesUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

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

  @Override
  public void execute(List<ControllerNode> controllerNodeList) {
        FileWriter docFileWriter = null;
        try {
            final Template ctrlTemplate = getDocTpl();
            final String docFileName = String.format("%s-%s-api-docs.md",  DocContext.getDocsConfig().getProjectName(), DocContext.getDocsConfig().getApiVersion());
            final File docFile = new File(DocContext.getDocPath(), docFileName);
            docFileWriter = new FileWriter(docFile);
            Map<String, Object> data = new HashMap<>();
            data.put("controllerNodes", controllerNodeList);
            data.put("currentApiVersion", DocContext.getCurrentApiVersion());
            data.put("projectName", DocContext.getDocsConfig().getProjectName());
            data.put("i18n", DocContext.getI18n());
            ctrlTemplate.process(data, docFileWriter);
        } catch (TemplateException | IOException ex) {
            ex.printStackTrace();
        } finally {
            CommonUtils.closeSilently(docFileWriter);
        }
    }

    private Template getDocTpl() throws IOException {
        return ResourcesUtils.getFreemarkerTemplate("api-doc.md.ftl");
    }
}
