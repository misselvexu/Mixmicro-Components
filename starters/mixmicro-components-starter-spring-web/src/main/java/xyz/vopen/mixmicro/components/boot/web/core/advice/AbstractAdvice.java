package xyz.vopen.mixmicro.components.boot.web.core.advice;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import xyz.vopen.mixmicro.components.boot.web.MixmicroWebConfigProperties;
import xyz.vopen.mixmicro.kits.Assert;

import java.util.List;

/**
 * {@link AbstractAdvice}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/8
 */
public abstract class AbstractAdvice implements BeanFactoryAware, EnvironmentAware {

  protected BeanFactory beanFactory;

  protected Environment environment;

  protected MixmicroWebConfigProperties configProperties;

  private String contextPath;

  protected MixmicroWebConfigProperties getProperties() {
    if (configProperties == null) {
      try {
        this.configProperties = this.beanFactory.getBean(MixmicroWebConfigProperties.class);
      } catch (Exception ignore) {
      }
    }
    Assert.notNull(
        this.configProperties,
        "Context evn properties @MixmicroWebConfigProperties must be initialized with spring bean factory.");
    return this.configProperties;
  }

  /**
   * Callback that supplies the owning factory to a bean instance.
   *
   * <p>Invoked after the population of normal bean properties but before an initialization callback
   * such as {@link InitializingBean#afterPropertiesSet()} or a custom init-method.
   *
   * @param beanFactory owning BeanFactory (never {@code null}). The bean can immediately call
   *     methods on the factory.
   * @throws BeansException in case of initialization errors
   * @see BeanInitializationException
   */
  @Override
  public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  /**
   * Set the {@code Environment} that this component runs in.
   *
   * @param environment env
   */
  @Override
  public void setEnvironment(@NonNull Environment environment) {
    this.environment = environment;
  }

  private String cleanContextPath(String contextPath) {
    String candidate = StringUtils.trimWhitespace(contextPath);
    if (StringUtils.hasText(candidate) && candidate.endsWith("/")) {
      return candidate.substring(0, candidate.length() - 1);
    }
    return candidate;
  }

  protected boolean checkIgnoreURL(@NonNull String path) {

    if(this.contextPath == null) {
      this.contextPath = environment.getProperty("server.servlet.context-path","");
      this.contextPath = cleanContextPath(this.contextPath);
    }

    List<String> urls = getProperties().getResponse().getIgnoreUris();
    for (String url : urls) {
      if(url.startsWith(this.contextPath.concat(path))) {
        return true;
      }
    }
    return false;
  }
}
