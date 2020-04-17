package xyz.vopen.mixmicro.components.boot.openfeign;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;
import java.util.Set;

import static xyz.vopen.mixmicro.components.boot.openfeign.OpenFeignConfigProperties.OPENFEIGN_PROPERTIES_PREFIX;

/**
 * {@link OpenFeignConfigProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/5
 */
@Getter
@Setter
@ConfigurationProperties(prefix = OPENFEIGN_PROPERTIES_PREFIX)
public class OpenFeignConfigProperties implements Serializable {

  public static final String OPENFEIGN_PROPERTIES_PREFIX = "mixmicro.feign";

  @NestedConfigurationProperty private TransportMetadata metadata = new TransportMetadata();

  @Data
  public static class TransportMetadata implements Serializable, InitializingBean {

    private final Set<String> DEFAULT_ENVS = Sets.newHashSet();

    /**
     * Env Properties Keys . Feign Interceptor (Read value from service evn configure properties .)
     *
     * <p>
     */
    private Set<String> envKeys = Sets.newHashSet();

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties and
     * satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     *
     * <p>This method allows the bean instance to perform validation of its overall configuration
     * and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an essential
     *     property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {
      envKeys.addAll(DEFAULT_ENVS);
    }
  }
}
