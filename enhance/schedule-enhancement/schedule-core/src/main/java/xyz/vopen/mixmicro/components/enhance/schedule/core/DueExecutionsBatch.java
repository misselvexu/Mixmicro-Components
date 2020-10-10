package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

class DueExecutionsBatch {

  private static final Logger log = LoggerFactory.getLogger(DueExecutionsBatch.class);
  private final int generationNumber;
  private final AtomicInteger executionsLeftInBatch;
  private final int threadpoolSize;
  private final boolean possiblyMoreExecutionsInDb;
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

  public void oneExecutionDone(Runnable triggerCheckForNewBatch) {

    executionsLeftInBatch.decrementAndGet();

    log.trace(
        "Batch state: generationNumber:{}, stale:{}, triggeredExecuteDue:{}, possiblyMoreExecutionsInDb:{}, executionsLeftInBatch:{}, ratio-trigger:{}",
        generationNumber,
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
      log.trace("Triggering check for new batch.");
      triggerCheckForNewBatch.run();
      triggeredExecuteDue = true;
    }
  }

  public boolean isOlderGenerationThan(int compareTo) {
    return generationNumber < compareTo;
  }

  public int getGenerationNumber() {
    return generationNumber;
  }
}
