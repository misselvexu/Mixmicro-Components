package xyz.vopen.mixmicro.components.enhance.apidoc;


import xyz.vopen.mixmicro.components.enhance.apidoc.model.ResponseNode;

import java.util.Map;

/**
 * wrap response into a common structure, you should put the response into a map ,
 *
 * <p>for now this just use for upload apis to rap.
 *
 * <p>default is :{ code : 0, data: ${response} msg: 'success' }
 *
 * <p>/**
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public interface IResponseWrapper {

  /**
   * to wrap response , don't forget to put responseNode into map.
   *
   * @param responseNode response node
   */
  Map<String, Object> wrapResponse(ResponseNode responseNode);
}
