package xyz.vopen.mixmicro.components.enhance.cache.support;

import java.util.List;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class StatInfo {
  private List<CacheStat> stats;
  private long startTime;
  private long endTime;

  public List<CacheStat> getStats() {
    return stats;
  }

  public void setStats(List<CacheStat> stats) {
    this.stats = stats;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }
}
