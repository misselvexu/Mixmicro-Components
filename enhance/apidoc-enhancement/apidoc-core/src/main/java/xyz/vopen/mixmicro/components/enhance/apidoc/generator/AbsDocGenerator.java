package xyz.vopen.mixmicro.components.enhance.apidoc.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.builder.IControllerDocBuilder;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.LinkModel;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.RequestNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.parser.AbsControllerParser;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public abstract class AbsDocGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbsDocGenerator.class);
  protected AbsControllerParser controllerParser;
  protected IControllerDocBuilder controllerDocBuilder;
  protected final List<LinkModel> docFileLinkList = new ArrayList<>();
  protected List<ControllerNode> controllerNodeList = new ArrayList<>();

  protected AbsDocGenerator(
      AbsControllerParser controllerParser, IControllerDocBuilder controllerDocBuilder) {
    this.controllerParser = controllerParser;
    this.controllerDocBuilder = controllerDocBuilder;
    this.initControllerNodes();
  }

  protected AbsDocGenerator() {}

  /** generate api Docs */
  public void generateDocs() {
    LOGGER.info("generate api docs start...");
    generateControllersDocs();
    generateIndex(controllerNodeList);
    LOGGER.info("generate api docs done !!!");
  }

  public void initControllerNodes() {
    File[] controllerFiles = DocContext.getControllerFiles();
    for (File controllerFile : controllerFiles) {
      LOGGER.info("start to parse controller file : {}", controllerFile.getName());
      ControllerNode controllerNode = controllerParser.parse(controllerFile);
      if (controllerNode.getRequestNodes().isEmpty()) {
        continue;
      }

      controllerNode.setSrcFileName(controllerFile.getAbsolutePath());
      final String docFileName =
          String.format(
              "%s_%s.html",
              controllerNode.getPackageName().replace(".", "_"), controllerNode.getClassName());
      controllerNode.setDocFileName(docFileName);
      for (RequestNode requestNode : controllerNode.getRequestNodes()) {
        requestNode.setCodeFileUrl(
            String.format("%s#%s", docFileName, requestNode.getMethodName()));
      }

      controllerNodeList.add(controllerNode);
      LOGGER.info("success to parse controller file : {}", controllerFile.getName());
    }
  }

  private void generateControllersDocs() {
    File docPath = new File(DocContext.getDocPath());
    for (ControllerNode controllerNode : controllerNodeList) {
      try {
        LOGGER.info(
            "start to generate docs for controller file : {}", controllerNode.getSrcFileName());
        final String controllerDocs = controllerDocBuilder.buildDoc(controllerNode);
        docFileLinkList.add(
            new LinkModel(
                controllerNode.getDescription(),
                String.format("%s", controllerNode.getDocFileName())));
        CommonUtils.writeToDisk(new File(docPath, controllerNode.getDocFileName()), controllerDocs);
        LOGGER.info(
            "success to generate docs for controller file : {}", controllerNode.getSrcFileName());
      } catch (IOException e) {
        LOGGER.error(
            "generate docs for controller file : " + controllerNode.getSrcFileName() + " fail", e);
      }
    }
  }

  public List<ControllerNode> getControllerNodeList() {
    return controllerNodeList;
  }

  protected abstract void generateIndex(List<ControllerNode> controllerNodeList);
}
