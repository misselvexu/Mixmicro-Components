package xyz.vopen.mixmicro.components.authorization.api;

import xyz.vopen.mixmicro.components.authorization.AccessToken;
import xyz.vopen.mixmicro.components.authorization.Payload;

/**
 * {@link AuthorizationApi}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-10.
 */
public interface AuthorizationApi<P extends Payload> {

  /**
   * Generate New Access Token With Defined {@link Payload}
   *
   * @param payload payload instance
   * @return instance of {@link AccessToken}
   */
  AccessToken<P> generateAccessToken(P payload);

  
  P getPayload(Class<P> clazz);
}
