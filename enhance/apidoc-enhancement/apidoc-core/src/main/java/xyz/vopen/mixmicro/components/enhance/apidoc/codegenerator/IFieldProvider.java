package xyz.vopen.mixmicro.components.enhance.apidoc.codegenerator;

import xyz.vopen.mixmicro.components.enhance.apidoc.model.ClassNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.FieldModel;

import java.util.List;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public interface IFieldProvider {
  /**
   * get response fields
   *
   * @param respNode
   * @return
   */
  List<FieldModel> provideFields(ClassNode respNode);
}
