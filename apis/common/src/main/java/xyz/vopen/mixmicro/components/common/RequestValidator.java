package xyz.vopen.mixmicro.components.common;

import xyz.vopen.mixmicro.components.exception.defined.InvalidRequestParamException;

/**
 * {@link RequestValidator}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-05-31.
 */
public interface RequestValidator {

  /**
   * Request Params Validate Action Method .
   *
   * @throws InvalidRequestParamException if parameter is invalid, thrown {@link
   *     InvalidRequestParamException}
   */
  default void validate() throws InvalidRequestParamException {}
}
