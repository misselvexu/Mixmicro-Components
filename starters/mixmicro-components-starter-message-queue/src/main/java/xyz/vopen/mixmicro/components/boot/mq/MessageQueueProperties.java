package xyz.vopen.mixmicro.components.boot.mq;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static xyz.vopen.mixmicro.components.boot.mq.MessageQueueProperties.MESSAGE_QUEUE_CONFIG_PROPERTIES_PREFIX;

/**
 * {@link MessageQueueProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/8
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = MESSAGE_QUEUE_CONFIG_PROPERTIES_PREFIX)
public class MessageQueueProperties implements Serializable {

  public static final String MESSAGE_QUEUE_CONFIG_PROPERTIES_PREFIX = "mixmicro.message.queue";

  /**
   * Server Addrs
   *
   * <p>
   */
  private List<String> serverAddr = Lists.newArrayList();

  /**
   * Producer Configs
   *
   * <p>
   */
  private Map<String, ProducerConfig> producers = Maps.newHashMap();

  /**
   * Consumer Configs
   *
   * <p>
   */
  private Map<String, ConsumerConfig> consumers = Maps.newHashMap();

  // ==== Static Config Class ====

  @Data
  public static class ProducerConfig implements Serializable {}

  @Data
  public static class ConsumerConfig implements Serializable {}
}
