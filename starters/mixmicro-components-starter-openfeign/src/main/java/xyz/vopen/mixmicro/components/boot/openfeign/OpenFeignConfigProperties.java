package xyz.vopen.mixmicro.components.boot.openfeign;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
public class OpenFeignConfigProperties implements Serializable, InitializingBean {

  public static final String OPENFEIGN_PROPERTIES_PREFIX = "mixmicro.feign";

  @NestedConfigurationProperty private TransportMetadata metadata = new TransportMetadata();

  @Override
  public void afterPropertiesSet() throws Exception {
    metadata.afterPropertiesSet();
  }

  @Data
  public static class TransportMetadata implements Serializable, InitializingBean {

    @Getter(AccessLevel.PRIVATE)
    private final Set<String> DEFAULT_ENVS = Sets.newHashSet();

    /**
     * Env Properties Keys . Feign Interceptor (Read value from service evn configure properties .)
     *
     * <p>
     */
    private Set<String> envKeys = Sets.newHashSet();

    @Override
    public void afterPropertiesSet() throws Exception {
      envKeys.addAll(DEFAULT_ENVS);
    }
  }
}
