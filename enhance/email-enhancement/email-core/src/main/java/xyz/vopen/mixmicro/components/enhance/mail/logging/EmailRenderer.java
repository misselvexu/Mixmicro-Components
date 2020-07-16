package xyz.vopen.mixmicro.components.enhance.mail.logging;

import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;

/**
 * {@link EmailRenderer}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface EmailRenderer {

  /**
   * Render Email String
   *
   * @param email instance of {@link MixmicroEmail}
   * @return render result
   */
  String render(MixmicroEmail email);
}
