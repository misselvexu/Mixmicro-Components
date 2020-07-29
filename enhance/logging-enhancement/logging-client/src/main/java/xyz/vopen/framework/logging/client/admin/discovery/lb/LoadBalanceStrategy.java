package xyz.vopen.framework.logging.client.admin.discovery.lb;

import xyz.vopen.framework.logging.client.MinBoxLoggingException;

/**
 * Load balance strategy
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface LoadBalanceStrategy {
  /**
   * lookup Load-balanced addresses
   *
   * @param adminAddress logging admin address array
   * @return Load-balanced addresses
   * @throws MinBoxLoggingException MinBox Logging Exception
   */
  String lookup(String[] adminAddress) throws MinBoxLoggingException;
}
