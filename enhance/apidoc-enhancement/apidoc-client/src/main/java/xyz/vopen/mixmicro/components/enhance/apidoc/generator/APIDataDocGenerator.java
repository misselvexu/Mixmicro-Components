package xyz.vopen.mixmicro.components.enhance.apidoc.generator;

import org.springframework.util.CollectionUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocsConfig;
import xyz.vopen.mixmicro.components.enhance.apidoc.bean.IndexBean;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.repository.APIDocGenRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/1
 */
public class APIDataDocGenerator extends AbsDocGenerator implements APIGenerator {

  private final APIDocGenRepository apiDocGenRepository;

  public APIDataDocGenerator(APIDocGenRepository apiDocGenRepository) {
    this.apiDocGenRepository = apiDocGenRepository;
  }

  /**
   * generate apidoc
   *
   * @param config config
   */
  @Override
  public void generate(DocsConfig config) {
    DocContext.initForDataGenerator(config);
    super.controllerParser = DocContext.controllerParser();
    super.initControllerNodes();
    if (!CollectionUtils.isEmpty(config.getIgnorePackages())) {
      super.controllerNodeList =
          super.controllerNodeList.stream()
              .filter(
                  controllerNode ->
                      !config.getIgnorePackages().contains(controllerNode.getPackageName()))
              .collect(Collectors.toList());
      DocContext.setControllerNodeList(super.controllerNodeList);
    }
    generateIndex(controllerNodeList);
  }

  @Override
  public void generateIndex(List<ControllerNode> controllerNodeList) {
    IndexBean indexBean = new IndexBean();
    indexBean.setProjectName(DocContext.getDocsConfig().getProjectName());
    indexBean.setCurrentApiVersion(DocContext.getCurrentApiVersion());
    String indexId = apiDocGenRepository.crateIndexIfAbsent(indexBean);
    apiDocGenRepository.createControllerNode(indexId, controllerNodeList);
  }
}
