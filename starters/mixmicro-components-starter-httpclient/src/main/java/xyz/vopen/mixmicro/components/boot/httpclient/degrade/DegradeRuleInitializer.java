package xyz.vopen.mixmicro.components.boot.httpclient.degrade;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import xyz.vopen.mixmicro.components.boot.httpclient.autoconfigure.MixHttpClientProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * {@link DegradeRuleInitializer}
 *
 * <p>Class DegradeRuleInitializer Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
public class DegradeRuleInitializer implements ApplicationListener<ApplicationReadyEvent> {

  private static final Logger logger = LoggerFactory.getLogger(DegradeRuleInitializer.class);

  private final MixHttpClientProperties properties;

  private static final List<DegradeRule> LIST = new CopyOnWriteArrayList<>();

  public DegradeRuleInitializer(MixHttpClientProperties properties) {
    this.properties = properties;
  }

  public static void addRetrofitDegradeRule(DegradeRule degradeRule) {
    if (degradeRule == null) {
      return;
    }
    LIST.add(degradeRule);
  }

  /**
   * Handle an application event.
   *
   * @param event the event to respond to
   */
  @Override
  public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
    if (!properties.isEnableDegrade()) {
      return;
    }

    DegradeType degradeType = properties.getDegradeType();

    switch (degradeType) {

      case SENTINEL:
        try {
          Class.forName("com.alibaba.csp.sentinel.SphU");
          List<com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule> rules = new ArrayList<>();

          for (DegradeRule degradeRule : LIST) {
            DegradeStrategy degradeStrategy = degradeRule.getDegradeStrategy();
            int grade;
            switch (degradeStrategy) {
              case AVERAGE_RT:
                grade = 0;
                break;
              case EXCEPTION_RATIO:
                grade = 1;
                break;
              default:
                throw new IllegalArgumentException(
                    "Not currently supported! degradeStrategy=" + degradeStrategy);
            }
            String resourceName = degradeRule.getResourceName();
            // add degrade rule
            com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule rule =
                new com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule()
                    .setGrade(grade)
                    // Max allowed response time
                    .setCount(degradeRule.getCount())
                    // Retry timeout (in second)
                    .setTimeWindow(degradeRule.getTimeWindow());

            rule.setResource(resourceName);
            rules.add(rule);
          }
          DegradeRuleManager.loadRules(rules);

        } catch (Exception e) {
          logger.warn("com.alibaba.csp.sentinel not found! No SentinelDegradeInterceptor is set.");
        }
        break;
      default:
        throw new IllegalArgumentException("Not currently supported! degradeType=" + degradeType);
    }
  }
}
