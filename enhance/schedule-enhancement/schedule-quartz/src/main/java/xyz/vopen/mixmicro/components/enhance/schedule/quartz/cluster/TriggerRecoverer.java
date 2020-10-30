package xyz.vopen.mixmicro.components.enhance.schedule.quartz.cluster;

import xyz.vopen.mixmicro.components.enhance.schedule.quartz.LockManager;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.TriggerAndJobPersister;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.JobRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.LocksRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.TriggerRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.trigger.MisfireHandler;
import org.quartz.JobPersistenceException;
import org.quartz.TriggerKey;
import org.quartz.spi.OperableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerRecoverer {

  private static final Logger log = LoggerFactory.getLogger(TriggerRecoverer.class);

  private final LocksRepository locksRepository;
  private final TriggerAndJobPersister persister;
  private final LockManager lockManager;
  private final TriggerRepository triggerRepository;
  private final JobRepository jobRepository;
  private final RecoveryTriggerFactory recoveryTriggerFactory;
  private final MisfireHandler misfireHandler;

  public TriggerRecoverer(
      LocksRepository locksRepository,
      TriggerAndJobPersister persister,
      LockManager lockManager,
      TriggerRepository triggerRepository,
      JobRepository jobRepository,
      RecoveryTriggerFactory recoveryTriggerFactory,
      MisfireHandler misfireHandler) {
    this.locksRepository = locksRepository;
    this.persister = persister;
    this.lockManager = lockManager;
    this.triggerRepository = triggerRepository;
    this.jobRepository = jobRepository;
    this.recoveryTriggerFactory = recoveryTriggerFactory;
    this.misfireHandler = misfireHandler;
  }

  public void recover() throws JobPersistenceException {
    for (TriggerKey key : locksRepository.findOwnTriggersLocks()) {
      OperableTrigger trigger = triggerRepository.getTrigger(key);
      if (trigger == null) {
        continue;
      }

      // Make the trigger's lock fresh for other nodes,
      // so they don't recover it.
      if (locksRepository.updateOwnLock(trigger.getKey())) {
        doRecovery(trigger);
        lockManager.unlockAcquiredTrigger(trigger);
      }
    }
  }

  /**
   * Do recovery procedure after failed run of given trigger.
   *
   * @param trigger trigger to recover
   * @return recovery trigger or null if its job doesn't want that
   * @throws JobPersistenceException
   */
  public OperableTrigger doRecovery(OperableTrigger trigger) throws JobPersistenceException {
    OperableTrigger recoveryTrigger = null;
    if (jobRepository.requestsRecovery(trigger.getJobKey())) {
      recoveryTrigger = recoverTrigger(trigger);
      if (!wasOneShotTrigger(trigger)) {
        updateMisfires(trigger);
      }
    } else if (wasOneShotTrigger(trigger)) {
      cleanUpFailedRun(trigger);
    } else {
      updateMisfires(trigger);
    }
    return recoveryTrigger;
  }

  private OperableTrigger recoverTrigger(OperableTrigger trigger) throws JobPersistenceException {
    log.info("Recovering trigger: {}", trigger.getKey());
    OperableTrigger recoveryTrigger = recoveryTriggerFactory.from(trigger);
    persister.storeTrigger(recoveryTrigger, false);
    return recoveryTrigger;
  }

  private void updateMisfires(OperableTrigger trigger) throws JobPersistenceException {
    if (misfireHandler.applyMisfireOnRecovery(trigger)) {
      log.info("Misfire applied. Replacing trigger: {}", trigger.getKey());
      persister.storeTrigger(trigger, true);
    } else {
      // TODO should complete trigger?
      log.warn("Recovery misfire not applied for trigger: {}", trigger.getKey());
      //            storeTrigger(conn, trig,
      //                    null, true, STATE_COMPLETE, forceState, recovering);
      //            schedSignaler.notifySchedulerListenersFinalized(trig);
    }
  }

  private void cleanUpFailedRun(OperableTrigger trigger) {
    persister.removeTrigger(trigger.getKey());
  }

  private boolean wasOneShotTrigger(OperableTrigger trigger) {
    return trigger.getNextFireTime() == null
        && trigger.getStartTime().equals(trigger.getFinalFireTime());
  }
}
