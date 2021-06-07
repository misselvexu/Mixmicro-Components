package xyz.vopen.mixmicro.components.enhance.apidoc.genetator;

import xyz.vopen.mixmicro.components.enhance.apidoc.builder.JavaClassBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.builder.JavaFieldBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.builder.JavaGetterBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.builder.JavaSetterBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.generator.CodeGenerator;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ClassNodeProxy;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.FieldModel;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ResponseNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.provider.IFieldProvider;
import xyz.vopen.mixmicro.components.enhance.apidoc.provider.JavaTemplateProvider;
import xyz.vopen.mixmicro.components.enhance.apidoc.provider.ProviderFactory;
import xyz.vopen.mixmicro.components.enhance.apidoc.provider.TemplateProvider;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class JavaCodeGenerator extends CodeGenerator {

  private static final String FILE_FIELD_TEMPLATE = "Java_Entity_Field.tpl";
  private static final String FILE_GETTER_TEMPLATE = "Java_Entity_Getter.tpl";
  private static final String FILE_SETTER_TEMPLATE = "Java_Entity_Setter.tpl";
  private static final String FILE_CLASS_TEMPLATE = "Java_Entity.tpl";
  private static final String FILE_CODE_TEMPLATE = "Code_File.html.tpl";
  private static final String JAVA_CODE_DIR = "javaCodes";

  private static String sFieldTemplate,
      sGetterTemplate,
      sSetterTemplate,
      sClassTemplate,
      sCodeTemplate;

  static {
    JavaTemplateProvider resourceTemplateProvider = new JavaTemplateProvider();
    try {
      sFieldTemplate = resourceTemplateProvider.provideTemplateForName(FILE_FIELD_TEMPLATE);
      sGetterTemplate = resourceTemplateProvider.provideTemplateForName(FILE_GETTER_TEMPLATE);
      sSetterTemplate = resourceTemplateProvider.provideTemplateForName(FILE_SETTER_TEMPLATE);
      sClassTemplate = resourceTemplateProvider.provideTemplateForName(FILE_CLASS_TEMPLATE);
      sCodeTemplate = TemplateProvider.provideTemplateForName(FILE_CODE_TEMPLATE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public JavaCodeGenerator(ResponseNode responseNode) {
    super(responseNode);
  }

  @Override
  public String generateNodeCode(ClassNodeProxy respNode) {
    String className = respNode.getClassName();

    IFieldProvider entryProvider = ProviderFactory.createProvider();
    List<FieldModel> entryFields = entryProvider.provideFields(respNode);

    if (entryFields == null || entryFields.isEmpty()) {
      return "";
    }

    StringBuilder fieldStrings = new StringBuilder();
    StringBuilder methodStrings = new StringBuilder();

    String fieldTemplate = sFieldTemplate;
    String getterTemplate = sGetterTemplate;
    String setterTemplate = sSetterTemplate;
    String classTemplate = sClassTemplate;

    for (FieldModel entryFieldModel : entryFields) {
      JavaFieldBuilder fieldBuilder = new JavaFieldBuilder(fieldTemplate, entryFieldModel);
      fieldStrings.append(fieldBuilder.build());
      JavaGetterBuilder getterBuilder = new JavaGetterBuilder(getterTemplate, entryFieldModel);
      methodStrings.append(getterBuilder.build());
      JavaSetterBuilder setterBuilder = new JavaSetterBuilder(setterTemplate, entryFieldModel);
      methodStrings.append(setterBuilder.build());
    }

    if (methodStrings.charAt(methodStrings.length() - 1) == '\n') {
      methodStrings.deleteCharAt(methodStrings.length() - 1);
    }

    JavaClassBuilder classBuilder =
        new JavaClassBuilder(
            classTemplate, className, fieldStrings.toString(), methodStrings.toString());
    return classBuilder.build();
  }

  @Override
  public String getRelativeCodeDir() {
    return JAVA_CODE_DIR;
  }

  @Override
  public String getCodeTemplate() {
    return sCodeTemplate;
  }
}
