package xyz.vopen.mixmicro.components.enhance.mail.configuration;

import com.google.common.base.Preconditions;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static xyz.vopen.mixmicro.components.enhance.mail.configuration.ApplicationPropertiesConstants.__SPRING_MAIL_SCHEDULER;
import static java.util.Objects.isNull;

/**
 * {@link EmailSchedulerProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/15/20
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = __SPRING_MAIL_SCHEDULER)
public class EmailSchedulerProperties {

  private boolean enabled;

  // spring.mail.scheduler.priorityLevels
  private Integer priorityLevels = 10;

  // spring.mail.scheduler.persistence.*
  @NestedConfigurationProperty private Persistence persistence = new Persistence();

  @Builder
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Persistence {

    private boolean enabled;

    @NestedConfigurationProperty private Redis redis = new Redis();

    // spring.mail.scheduler.persistence.desiredBatchSize
    private int desiredBatchSize = 500;

    // spring.mail.scheduler.persistence.minKeptInMemory
    private int minKeptInMemory = 250;

    // spring.mail.scheduler.persistence.maxKeptInMemory
    private int maxKeptInMemory = 2000;

  }

  @Builder
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Redis {

    private boolean enabled;

    private boolean embedded;
  }

  @PostConstruct
  protected boolean validate() {
    if (enabled) {
      checkIsValid(this);
    } else {
      setValuesToNull();
    }
    return true;
  }

  public static void checkIsValid(@NonNull final EmailSchedulerProperties emailSchedulerProperties) {
    Preconditions.checkState(emailSchedulerProperties.getPriorityLevels() > 0,
        "Expected at least one priority level. Review property 'spring.mail.scheduler.priorityLevels'.");

    Preconditions.checkState(isNull(emailSchedulerProperties.getPersistence()) || emailSchedulerProperties.getPersistence().getDesiredBatchSize() > 0,
        "Expected at least a batch of size one, otherwise the persistence layer will not work. Review property 'spring.mail.scheduler.persistence.desiredBatchSize'.");

    Preconditions.checkState(isNull(emailSchedulerProperties.getPersistence()) || emailSchedulerProperties.getPersistence().getMinKeptInMemory() >= 0,
        "Expected a non negative amount of email to be kept in memory. Review property 'spring.mail.scheduler.persistence.minKeptInMemory'.");

    Preconditions.checkState(isNull(emailSchedulerProperties.getPersistence()) || emailSchedulerProperties.getPersistence().getMaxKeptInMemory() > 0,
        "Expected at least one email to be available in memory, otherwise the persistence layer will not work. Review property 'spring.mail.scheduler.persistence.maxKeptInMemory'.");

    Preconditions.checkState(isNull(emailSchedulerProperties.getPersistence()) ||
            (emailSchedulerProperties.getPersistence().getMaxKeptInMemory() >= emailSchedulerProperties.getPersistence().getMinKeptInMemory()),
        "The application properties key '%s' should not have a value smaller than the value in property '%s'.",
        "spring.mail.scheduler.persistence.maxKeptInMemory", "spring.mail.scheduler.persistence.minKeptInMemory");

    Preconditions.checkState(isNull(emailSchedulerProperties.getPersistence()) ||
            (emailSchedulerProperties.getPersistence().getMaxKeptInMemory() >= emailSchedulerProperties.getPersistence().getDesiredBatchSize()),
        "The application properties key '%s' should not have a value smaller than the value in property '%s'.",
        "spring.mail.scheduler.persistence.maxKeptInMemory", "spring.mail.scheduler.persistence.desiredBatchSize");
  }

  private void setValuesToNull() {
    priorityLevels = null;
    persistence = null;
  }

}