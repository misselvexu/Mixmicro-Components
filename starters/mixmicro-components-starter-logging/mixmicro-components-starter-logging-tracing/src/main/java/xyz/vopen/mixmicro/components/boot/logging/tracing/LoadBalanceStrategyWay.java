package xyz.vopen.mixmicro.components.boot.logging.tracing;

import lombok.Getter;
import xyz.vopen.framework.logging.client.admin.discovery.lb.support.RandomWeightedStrategy;
import xyz.vopen.framework.logging.client.admin.discovery.lb.support.SmoothWeightedRoundRobinStrategy;

/**
 * logging strategy load balance away {@link LoadBalanceStrategyWay}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Getter
public enum LoadBalanceStrategyWay {
  /** random weight {@link RandomWeightedStrategy} */
  RANDOM_WEIGHT,
  /** pool weight {@link SmoothWeightedRoundRobinStrategy} */
  POLL_WEIGHT
}
