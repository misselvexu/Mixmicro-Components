package xyz.vopen.mixmicro.components.enhance.apidoc.provider;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class ProviderFactory {

  private ProviderFactory() {}

  public static IFieldProvider createProvider() {
    return new DocFieldProvider();
  }
}
