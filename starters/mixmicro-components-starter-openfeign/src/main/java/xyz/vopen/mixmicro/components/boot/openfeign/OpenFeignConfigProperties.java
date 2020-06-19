package xyz.vopen.mixmicro.components.boot.openfeign;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

  /**
   * Metadata Values
   *
   * <p>
   */
  @NestedConfigurationProperty private TransportMetadata metadata = new TransportMetadata();

  /**
   * Extension Attributes
   *
   * @since 1.0.5
   */
  private List<TransportAttribute> attributes = Lists.newArrayList();

  /**
   * Sensitive Headers Config(s)
   *
   * <p>
   * @since 1.0.5
   */
  private List<String> sensitiveHeaders = Lists.newArrayList();

  @Override
  public void afterPropertiesSet() throws Exception {
    metadata.afterPropertiesSet();
    if(!attributes.isEmpty()) {
      for (TransportAttribute attribute : attributes) {
        attribute.afterPropertiesSet();
      }
    }
  }

  public enum AttributeType {

    /**
     * Request Header
     *
     * <p>
     */
    REQUEST_HEADER,

    /**
     * Manual Header
     *
     * <p>
     */
    MANUAL;
  }

  @Data
  public static class TransportAttribute implements Serializable, InitializingBean {

    /**
     * Attribute Name
     *
     * <p>
     */
    private String name;

    /**
     * Default Properties
     *
     * <p>
     */
    private AttributeType type = AttributeType.MANUAL;

    @Override
    public void afterPropertiesSet() throws Exception {
      // empty implements
    }
  }

  @Data
  public static class TransportMetadata implements Serializable, InitializingBean {

    @Getter(AccessLevel.PRIVATE)
    private static final Map<String, String> DEFAULT_ENVS = Maps.newHashMap();

    static {
      DEFAULT_ENVS.put("appname","yunlsp.metadata.app-name");
    }

    /**
     * Evn Prefix Defined
     * <p>default: ""</p>
     */
    private String prefix = "";

    /**
     * Env Keys Sensitive
     */
    private boolean envKeySensitive = false;

    /**
     * Env Properties Keys . Feign Interceptor (Read value from service evn configure properties .)
     *
     * <p>
     */
    private Map<String, String> envKeys = Maps.newHashMap();

    @Override
    public void afterPropertiesSet() throws Exception {
      if (!DEFAULT_ENVS.isEmpty()) {
        DEFAULT_ENVS.forEach(
            (key, value) -> {
              if (!envKeys.containsKey(key)) {
                envKeys.put(key, value);
              }
            });
      }
    }
  }
}
