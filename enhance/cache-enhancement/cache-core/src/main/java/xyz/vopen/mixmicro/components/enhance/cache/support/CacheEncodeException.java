package xyz.vopen.mixmicro.components.enhance.cache.support;

import xyz.vopen.mixmicro.components.enhance.cache.CacheException;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheEncodeException extends CacheException {

  private static final long serialVersionUID = -1768444197009616269L;

  public CacheEncodeException(String message, Throwable cause) {
    super(message, cause);
  }
}
