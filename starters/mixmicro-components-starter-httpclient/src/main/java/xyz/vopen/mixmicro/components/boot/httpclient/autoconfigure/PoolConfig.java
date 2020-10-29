package xyz.vopen.mixmicro.components.boot.httpclient.autoconfigure;

/**
 * 连接池参数配置 Connection pool parameter configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class PoolConfig {

  private int maxIdleConnections;

  private long keepAliveSecond;

  public PoolConfig(int maxIdleConnections, long keepAliveSecond) {
    this.maxIdleConnections = maxIdleConnections;
    this.keepAliveSecond = keepAliveSecond;
  }

  public PoolConfig() {}

  public void setMaxIdleConnections(int maxIdleConnections) {
    this.maxIdleConnections = maxIdleConnections;
  }

  public void setKeepAliveSecond(long keepAliveSecond) {
    this.keepAliveSecond = keepAliveSecond;
  }

  public int getMaxIdleConnections() {
    return maxIdleConnections;
  }

  public long getKeepAliveSecond() {
    return keepAliveSecond;
  }
}
