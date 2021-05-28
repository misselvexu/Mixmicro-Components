package xyz.vopen.mixmicro.components.enhance.apidoc.builder;

import xyz.vopen.mixmicro.components.enhance.apidoc.parser.ControllerNode;

import java.io.IOException;

/**
 * an interface of build a controller api docs
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public interface IControllerDocBuilder {

  /**
   * build api docs and return as string
   *
   * @param controllerNode endpoint node data
   * @return doc path
   */
  String buildDoc(ControllerNode controllerNode) throws IOException;
}
