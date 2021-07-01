package xyz.vopen.mixmicro.components.boot.snowflake;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

import static xyz.vopen.mixmicro.components.boot.snowflake.SnowflakeProperties.PREFIX;

/**
 * {@link SnowflakeProperties}
 *
 * <p>Class SnowflakeProperties Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/6/17
 */
@Getter
@Setter
@ConfigurationProperties(prefix = PREFIX)
public class SnowflakeProperties implements Serializable {

  public static final String PREFIX = "mixmicro.snowflake";

  @Builder.Default private long workerId = 1;

  @Builder.Default private boolean auto = true;
}
