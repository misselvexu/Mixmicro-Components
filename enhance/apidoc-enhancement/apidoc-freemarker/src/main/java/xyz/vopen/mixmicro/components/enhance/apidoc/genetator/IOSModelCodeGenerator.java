package xyz.vopen.mixmicro.components.enhance.apidoc.genetator;

import xyz.vopen.mixmicro.components.enhance.apidoc.builder.IOSModelBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.builder.IOSModelFieldBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ClassNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ClassNodeProxy;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ResponseNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.provider.IOSModelTemplateProvider;
import xyz.vopen.mixmicro.components.enhance.apidoc.provider.TemplateProvider;
import xyz.vopen.mixmicro.components.enhance.apidoc.generator.CodeGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.provider.IFieldProvider;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.FieldModel;
import xyz.vopen.mixmicro.components.enhance.apidoc.provider.ProviderFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class IOSModelCodeGenerator extends CodeGenerator {

  private static final String FILE_FIELD_TEMPLATE = "IOS_Model_Field.tpl";
  private static final String FILE_MODEL_TEMPLATE = "IOS_Model.tpl";
  private static final String FILE_CODE_TEMPLATE = "Code_File.html.tpl";
  private static final String IOS_CODE_DIR = "iosCodes";

  private static String sFieldTemplate;
  private static String sModelTemplate;
  private static String sCodeTemplate;

  static {
    IOSModelTemplateProvider resourceTemplateProvider = new IOSModelTemplateProvider();
    try {
      sFieldTemplate = resourceTemplateProvider.provideTemplateForName(FILE_FIELD_TEMPLATE);
      sModelTemplate = resourceTemplateProvider.provideTemplateForName(FILE_MODEL_TEMPLATE);
      sCodeTemplate = TemplateProvider.provideTemplateForName(FILE_CODE_TEMPLATE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public IOSModelCodeGenerator(ResponseNode responseNode) {
    super(responseNode);
  }

  @Override
  public String generateNodeCode(ClassNodeProxy respNode) throws IOException {
    String className = respNode.getClassName();
    IFieldProvider entryProvider = ProviderFactory.createProvider();
    List<FieldModel> entryFields = entryProvider.provideFields(respNode);
    if (entryFields == null || entryFields.isEmpty()) {
      return "";
    }
    StringBuilder fieldStrings = new StringBuilder();
    for (FieldModel entryFieldModel : entryFields) {
      IOSModelFieldBuilder fieldBuilder = new IOSModelFieldBuilder(sFieldTemplate, entryFieldModel);
      fieldStrings.append(fieldBuilder.build());
    }
    IOSModelBuilder iosModelBuilder =
        new IOSModelBuilder(sModelTemplate, className, fieldStrings.toString());
    return iosModelBuilder.build();
  }

  @Override
  public String getRelativeCodeDir() {
    return IOS_CODE_DIR;
  }

  @Override
  public String getCodeTemplate() {
    return sCodeTemplate;
  }
}
