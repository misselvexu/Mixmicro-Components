package xyz.vopen.mixmicro.components.enhance.apidoc.generator;

import xyz.vopen.mixmicro.components.enhance.apidoc.DocsConfig;

/**
 * API generator interface
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/3
 */
public interface APIGenerator {

  /**
   * generate method
   *
   * @param config DocsConfig
   */
  void generate(DocsConfig config);
}
