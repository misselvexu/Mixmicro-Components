package xyz.vopen.mixmicro.components.enhance.apidoc.builder;

import xyz.vopen.mixmicro.components.enhance.apidoc.codegenerator.ICodeBuilder;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class JavaClassBuilder implements ICodeBuilder {

  private final String className;
  private final String mFieldCode;
  private final String mMethodCode;
  private String entryClassTemplate;

  public JavaClassBuilder(
      String entryClassTemplate, String className, String mFieldCode, String mMethodCode) {
    this.className = className;
    this.mFieldCode = mFieldCode;
    this.mMethodCode = mMethodCode;
    this.entryClassTemplate = entryClassTemplate;
  }

  @Override
  public String build() {
    entryClassTemplate = entryClassTemplate.replace("${CLASS_NAME}", className);
    entryClassTemplate = entryClassTemplate.replace("${FIELDS}", mFieldCode);
    entryClassTemplate = entryClassTemplate.replace("${METHODS}", mMethodCode);
    return entryClassTemplate;
  }
}
