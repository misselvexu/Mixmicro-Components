package xyz.vopen.mixmicro.components.enhance.apidoc.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class I18n {

  private final ResourceBundle resourceBundle;

  public I18n() {
    this.resourceBundle = ResourceBundle.getBundle("message", Locale.getDefault());
  }

  public I18n(Locale locale) {
    this.resourceBundle = ResourceBundle.getBundle("message", locale);
  }

  public String getMessage(String name) {
    return resourceBundle.getString(name);
  }
}
