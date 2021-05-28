package xyz.vopen.mixmicro.components.enhance.apidoc.provider;

import java.io.IOException;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class IOSModelTemplateProvider {

  public String provideTemplateForName(String templateName) throws IOException {
    return TemplateProvider.provideTemplateForName(templateName);
  }
}
