package xyz.vopen.mixmicro.components.enhance.mail.service.impl;

import com.google.common.base.Preconditions;

/**
 * {@link RedisBasedPersistenceServiceConstants}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class RedisBasedPersistenceServiceConstants {

  public static final String ORDERING_KEY_PREFIX = "priority-level:";

  public static String orderingKey(final int priorityLevel) {
    Preconditions.checkArgument(priorityLevel > 0, "Priority level must be a positive integer number");
    return orderingKeyPrefix() + priorityLevel;
  }

  public static String orderingKeyPrefix() {
    return ORDERING_KEY_PREFIX;
  }

}