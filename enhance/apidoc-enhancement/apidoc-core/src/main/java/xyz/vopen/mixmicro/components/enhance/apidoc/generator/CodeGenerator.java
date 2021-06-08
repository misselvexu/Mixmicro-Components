package xyz.vopen.mixmicro.components.enhance.apidoc.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.builder.CodeFileBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ClassNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.FieldNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ResponseNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public abstract class CodeGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(CodeGenerator.class);
  private final ResponseNode responseNode;
  private final File codePath;
  private final String codeRelativePath;

  protected CodeGenerator(ResponseNode responseNode) {
    this.responseNode = responseNode;
    this.codeRelativePath = getRelativeCodeDir();
    this.codePath = new File(DocContext.getDocPath(), codeRelativePath);
    if (!this.codePath.exists()) {
      boolean mkdirs = this.codePath.mkdirs();
      if (!mkdirs) {
        LOGGER.error("create {} folder failed", codePath);
      }
    }
  }

  /**
   * 生成代码
   *
   * @return 返回代码的相对目录
   * @throws IOException ex
   */
  public String generateCode() throws IOException {
    if (responseNode.getChildNodes() == null || responseNode.getChildNodes().isEmpty()) {
      return "";
    }
    StringBuilder codeBodyBuilder = new StringBuilder();
    generateCodeForBuilder(responseNode, codeBodyBuilder);
    final String sCodeTemplate = getCodeTemplate();
    CodeFileBuilder codeBuilder =
        new CodeFileBuilder(responseNode.getClassName(), codeBodyBuilder.toString(), sCodeTemplate);
    final String javaFileName =
        String.format(
            "%s_%s_%s_%s.html",
            responseNode.getControllerPackageName().replace(".", "_"),
            responseNode.getControllerClassName(),
            responseNode.getMethodName(),
            responseNode.getClassName());
    CommonUtils.writeToDisk(new File(codePath, javaFileName), codeBuilder.build());
    return String.format("%s/%s", codeRelativePath, javaFileName);
  }

  private void generateCodeForBuilder(ClassNode rootNode, StringBuilder codeBodyBuilder)
      throws IOException {
    codeBodyBuilder.append(generateNodeCode(rootNode));
    codeBodyBuilder.append('\n');
    for (FieldNode recordNode : rootNode.getChildNodes()) {
      if (recordNode.getChildNode() != null) {
        ClassNode fieldClassNode = new ClassNode();
        BeanUtils.copyProperties(recordNode.getChildNode(), fieldClassNode);
        generateCodeForBuilder(fieldClassNode, codeBodyBuilder);
      }
    }
  }

  /***
   * 产生单个ResponseNode节点的Code
   * @param classNode class node
   * @return code
   * @throws IOException IO exception
   */
  public abstract String generateNodeCode(ClassNode classNode) throws IOException;

  /**
   * 获取代码的写入的相对目录
   *
   * @return relative code folder path
   */
  public abstract String getRelativeCodeDir();

  /**
   * 获取最终的代码模板
   *
   * @return code template
   */
  public abstract String getCodeTemplate();
}
