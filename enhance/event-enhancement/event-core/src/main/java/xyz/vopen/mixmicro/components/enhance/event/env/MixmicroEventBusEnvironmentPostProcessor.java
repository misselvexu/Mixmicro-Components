package xyz.vopen.mixmicro.components.enhance.event.env;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.cloud.bus.BusEnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * The lowest precedence {@link EnvironmentPostProcessor} configures default RocketMQ Bus Properties
 * that will be appended into {@link SpringApplication#defaultProperties}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/18
 * @see BusEnvironmentPostProcessor
 */
@SuppressWarnings("ALL")
public class MixmicroEventBusEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

  /** The name of {@link PropertySource} of {@link SpringApplication#defaultProperties}. */
  private static final String PROPERTY_SOURCE_NAME = "defaultProperties";

  /** Mixmicro Event Bus Steam Input Channel Name Defined. */
  private static final String MIXMICRO_EVENT_BUS_INPUT_CHANNEL = "mixmicro-eventbus-input";

  /** Mixmicro Event Bus Steam Output Channel Name Defined. */
  private static final String MIXMICRO_EVENT_BUS_OUTPUT_CHANNEL = "mixmicro-eventbus-output";

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

    addDefaultPropertySource(environment);
  }

  private void addDefaultPropertySource(ConfigurableEnvironment environment) {

    Map<String, Object> map = new HashMap<>(8);

    configureDefaultProperties(map);

    addOrReplace(environment.getPropertySources(), map);
  }

  private void configureDefaultProperties(Map<String, Object> source) {
    String groupBindingPropertyName = createEventBusBindingPropertyName(MIXMICRO_EVENT_BUS_INPUT_CHANNEL, "group");
    String broadcastingPropertyName = createEventBusRMQStreamPropertyName(MIXMICRO_EVENT_BUS_INPUT_CHANNEL, "broadcasting");
    source.put(groupBindingPropertyName, "mixmicro-eventbus-rocketmq-group");
    source.put(broadcastingPropertyName, "true");
  }

  /**
   * Create new event bus stream properties
   *
   * @param channelName defined input channel name
   * @param propertyName properties key name
   * @return value of evn properties key
   */
  @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
  private String createEventBusRMQStreamPropertyName(String channelName, String propertyName) {
    return "spring.cloud.stream.rocketmq.bindings." + MIXMICRO_EVENT_BUS_INPUT_CHANNEL + ".consumer." + propertyName;
  }

  private String createEventBusBindingPropertyName(String channelName, String propertyName) {
    return "spring.cloud.stream.bindings." + channelName + "." + propertyName;
  }

  /**
   * Rebuild (add or replace) Evn PropertiesSource.
   *
   * @param propertySources {@link MutablePropertySources}
   * @param map Default RocketMQ Bus Properties
   * @see BusEnvironmentPostProcessor {@link BusEnvironmentPostProcessor#addOrReplace(MutablePropertySources, Map)}
   */
  private void addOrReplace(MutablePropertySources propertySources, Map<String, Object> map) {
    MapPropertySource target = null;
    if (propertySources.contains(PROPERTY_SOURCE_NAME)) {
      PropertySource<?> source = propertySources.get(PROPERTY_SOURCE_NAME);
      if (source instanceof MapPropertySource) {
        target = (MapPropertySource) source;
        for (String key : map.keySet()) {
          if (!target.containsProperty(key)) {
            target.getSource().put(key, map.get(key));
          }
        }
      }
    }
    if (target == null) {
      target = new MapPropertySource(PROPERTY_SOURCE_NAME, map);
    }
    if (!propertySources.contains(PROPERTY_SOURCE_NAME)) {
      propertySources.addLast(target);
    }
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }
}
