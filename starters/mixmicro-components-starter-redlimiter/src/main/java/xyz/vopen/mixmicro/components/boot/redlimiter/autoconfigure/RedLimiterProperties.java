package xyz.vopen.mixmicro.components.boot.redlimiter.autoconfigure;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

import static xyz.vopen.mixmicro.components.boot.redlimiter.autoconfigure.RedLimiterProperties.PREFIX;

/**
 * {@link RedLimiterProperties}
 *
 * <p>Class RedLimiterProperties Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/5/21
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = PREFIX)
public class RedLimiterProperties implements Serializable {

  public static final String PREFIX = "mixmicro.redlimiter";

  @Builder.Default private boolean enabled = true;

  // ~~ common properties

  private String serverAddr;

  @Builder.Default private String namespace = "public";

  @Builder.Default private String dataId = "mixmicro-limiter.yaml";

  @Builder.Default private String dataGroup = "DEFAULT_GROUP";

  @Builder.Default private String fileExtension = "yaml";

  @Builder.Default private long defaultTimeout = 30000L;

  // ~~ Repository Config Properties

  @NestedConfigurationProperty
  @Builder.Default private RepositoryProperties repository = new RepositoryProperties();

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RepositoryProperties implements Serializable {

    @Builder.Default private RepositoryType type = RepositoryType.REDIS;

  }


  public enum RepositoryType {
    REDIS
  }

}
