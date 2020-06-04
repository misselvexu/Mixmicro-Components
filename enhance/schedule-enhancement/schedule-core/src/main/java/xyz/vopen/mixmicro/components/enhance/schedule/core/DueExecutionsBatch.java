package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

class DueExecutionsBatch {

  private static final Logger LOG = LoggerFactory.getLogger(DueExecutionsBatch.class);
  private final int generationNumber;
  private final AtomicInteger executionsLeftInBatch;
  private int threadpoolSize;
  private boolean possiblyMoreExecutionsInDb;
  private boolean stale = false;
  private boolean triggeredExecuteDue;

  public DueExecutionsBatch(
      int threadpoolSize,
      int generationNumber,
      int executionsAdded,
      boolean possiblyMoreExecutionsInDb) {
    this.threadpoolSize = threadpoolSize;
    this.generationNumber = generationNumber;
    this.possiblyMoreExecutionsInDb = possiblyMoreExecutionsInDb;
    this.executionsLeftInBatch = new AtomicInteger(executionsAdded);
  }

  public void markBatchAsStale() {
    this.stale = true;
  }

  /** @param triggerCheckForNewBatch may be triggered more than one in racy conditions */
  public void oneExecutionDone(Supplier<Boolean> triggerCheckForNewBatch) {
    executionsLeftInBatch.decrementAndGet();

    LOG.trace(
        "Batch state: stale:{}, triggeredExecuteDue:{}, possiblyMoreExecutionsInDb:{}, executionsLeftInBatch:{}, ratio-trigger:{}",
        stale,
        triggeredExecuteDue,
        possiblyMoreExecutionsInDb,
        executionsLeftInBatch.get(),
        (threadpoolSize * Scheduler.TRIGGER_NEXT_BATCH_WHEN_AVAILABLE_THREADS_RATIO));
    if (!stale
        && !triggeredExecuteDue
        && possiblyMoreExecutionsInDb
        && executionsLeftInBatch.get()
            <= (threadpoolSize * Scheduler.TRIGGER_NEXT_BATCH_WHEN_AVAILABLE_THREADS_RATIO)) {
      triggeredExecuteDue = triggerCheckForNewBatch.get();
    }
  }

  public boolean isOlderGenerationThan(int compareTo) {
    return generationNumber < compareTo;
  }

  public int getGenerationNumber() {
    return generationNumber;
  }
}
