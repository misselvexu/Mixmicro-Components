package xyz.vopen.mixmicro.components.enhance.apidoc.codegenerator;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class CodeFileBuilder implements ICodeBuilder {

  private String fileName;
  private String codeBody;
  private String codeTemplate;

  public CodeFileBuilder(String fileName, String codeBody, String codeTemplate) {
    super();
    this.fileName = fileName;
    this.codeBody = codeBody;
    this.codeTemplate = codeTemplate;
  }

  public String build() {
    codeTemplate = codeTemplate.replace("${FILE_NAME}", fileName);
    codeTemplate = codeTemplate.replace("${MODEL_LIST}", codeBody);
    return codeTemplate;
  }
}
