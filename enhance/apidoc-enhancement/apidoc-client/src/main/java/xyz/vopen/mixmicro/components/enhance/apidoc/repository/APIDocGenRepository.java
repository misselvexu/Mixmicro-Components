package xyz.vopen.mixmicro.components.enhance.apidoc.repository;

import xyz.vopen.mixmicro.components.enhance.apidoc.bean.IndexBean;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * APIDocGenRepository
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/2
 */
public interface APIDocGenRepository {

  /**
   * crate index if not present else update
   *
   * @param indexBean project+version combine refers to unique
   * @return index id
   */
  String crateIndexIfAbsent(@NotNull IndexBean indexBean);

  /**
   * create controller node data
   *
   * @param controllerNodeList all controllerNode List
   */
  void createControllerNode(String indexId, @NotNull List<ControllerNode> controllerNodeList);
}
