package xyz.vopen.mixmicro.components.enhance.apidoc;

import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;

import java.util.List;

/**
 * an plugin interface, please feel free to to do what ever you want.
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public interface IPluginSupport {

  /**
   * a hook method
   *
   * @param controllerNodeList all the api data
   */
  void execute(List<ControllerNode> controllerNodeList);
}
