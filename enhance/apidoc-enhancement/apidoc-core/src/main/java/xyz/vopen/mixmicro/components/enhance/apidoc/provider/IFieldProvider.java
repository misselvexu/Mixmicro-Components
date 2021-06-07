package xyz.vopen.mixmicro.components.enhance.apidoc.provider;

import xyz.vopen.mixmicro.components.enhance.apidoc.model.ClassNodeProxy;
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
   * @param respNode response class node
   * @return filed model
   */
  List<FieldModel> provideFields(ClassNodeProxy respNode);
}
