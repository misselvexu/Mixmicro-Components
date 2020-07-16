package xyz.vopen.mixmicro.components.enhance.mail.configuration;

/**
 * {@link ApplicationPropertiesConstants}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ApplicationPropertiesConstants {

  private static final String DOT = ".";

  public static final String __SPRING_MAIL_SCHEDULER = "spring.mail.scheduler";

  public static final String __SPRING_MAIL_LOGGING = "spring.mail.logging";

  public static final String __SPRING_MAIL_LOGGING_STRATEGY = __SPRING_MAIL_LOGGING + DOT + "strategy";

  public static final String SPRING_MAIL_HOST = "spring.mail.host";

  public static final String SPRING_MAIL_PORT = "spring.mail.port";

  public static final String SPRING_MAIL_USERNAME = "spring.mail.username";

  public static final String SPRING_MAIL_PASSWORD = "spring.mail.password";

  public static final String SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH = "spring.mail.properties.mail.smtp.auth";

  public static final String SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE = "spring.mail.properties.mail.smtp.starttls.enable";

  public static final String SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_REQUIRED = "spring.mail.properties.mail.smtp.starttls.required";

  public static final String SPRING_MAIL_SCHEDULER_ENABLED = __SPRING_MAIL_SCHEDULER + DOT + "enabled";

  public static final String SPRING_MAIL_SCHEDULER_PRIORITY_LEVELS = __SPRING_MAIL_SCHEDULER + DOT + "priority-levels";

  public static final String SPRING_MAIL_PERSISTENCE_ENABLED = __SPRING_MAIL_SCHEDULER + DOT + "persistence.enabled";

  public static final String SPRING_MAIL_SCHEDULER_PERSISTENCE_DESIRED_BATCH_SIZE = __SPRING_MAIL_SCHEDULER + DOT + "persistence.desired-batch-size";

  public static final String SPRING_MAIL_SCHEDULER_PERSISTENCE_MIN_KEPT_IN_MEMORY = __SPRING_MAIL_SCHEDULER + DOT + "persistence.min-kept-inmemory";

  public static final String SPRING_MAIL_SCHEDULER_PERSISTENCE_MAX_KEPT_IN_MEMORY = __SPRING_MAIL_SCHEDULER + DOT + "persistence.max-kept-inmemory";

  public static final String SPRING_MAIL_SCHEDULER_PERSISTENCE_REDIS_ENABLED = __SPRING_MAIL_SCHEDULER + DOT + "persistence.redis.enabled";

  public static final String SPRING_MAIL_SCHEDULER_PERSISTENCE_REDIS_EMBEDDED = __SPRING_MAIL_SCHEDULER + DOT + "persistence.redis.embedded";

  public static final String SPRING_MAIL_SCHEDULER_PERSISTENCE_REDIS_HOST = __SPRING_MAIL_SCHEDULER + DOT + "persistence.redis.host";

  public static final String SPRING_MAIL_SCHEDULER_PERSISTENCE_REDIS_PORT = __SPRING_MAIL_SCHEDULER + DOT + "persistence.redis.port";

  public static final String SPRING_MAIL_SCHEDULER_PERSISTENCE_REDIS_SETTINGS = __SPRING_MAIL_SCHEDULER + DOT + "persistence.redis.settings";

  public static final String SPRING_MAIL_LOGGING_ENABLED = __SPRING_MAIL_LOGGING + DOT + "enabled";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_FROM = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "from";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_REPLY_TO = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "reply-to";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_TO = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "to";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_CC = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "cc";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_BCC = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "bcc";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_SUBJECT = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "subject";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_BODY = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "body";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_ATTACHMENTS = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "attachments";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_ENCODING = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "encoding";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_LOCALE = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "locale";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_SENT_AT = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "sentAt";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_RECEIPT_TO = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "receiptTo";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_DEPOSITION_NOTIFICATION_TO = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "deposition-notification-to";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_IGNORE_CUSTOM_HEADERS = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "ignore.custom-headers";

  public static final String SPRING_MAIL_LOGGING_STRATEGY_IGNORE_NULL_AND_EMPTY_COLLECTIONS = __SPRING_MAIL_LOGGING_STRATEGY + DOT + "ignore.null-and-empty-collections";

}
