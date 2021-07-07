package xyz.vopen.mixmicro.components.enhance.apidoc.provider;

import xyz.vopen.mixmicro.components.enhance.apidoc.utils.ResourcesUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.CommonUtils;

import java.io.IOException;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class TemplateProvider {

  private TemplateProvider() {}

  public static String provideTemplateForName(String templateName) throws IOException {
    return CommonUtils.streamToString(ResourcesUtils.getCodeTemplateFile(templateName));
  }
}
