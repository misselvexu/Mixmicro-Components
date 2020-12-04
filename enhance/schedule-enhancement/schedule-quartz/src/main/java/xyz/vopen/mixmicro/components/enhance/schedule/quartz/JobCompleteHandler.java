package xyz.vopen.mixmicro.components.enhance.schedule.quartz;

import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.JobRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.LocksRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.TriggerRepository;
import org.quartz.JobDetail;
import org.quartz.JobPersistenceException;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.SchedulerSignaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO Think of some better name for doing work after a job has completed :-)
public class JobCompleteHandler {

  private static final Logger log = LoggerFactory.getLogger(JobCompleteHandler.class);

  private final TriggerAndJobPersister persister;
  private final SchedulerSignaler signaler;
  private final JobRepository jobRepository;
  private final LocksRepository locksRepository;
  private TriggerRepository triggerRepository;

  public JobCompleteHandler(
      TriggerAndJobPersister persister,
      SchedulerSignaler signaler,
      JobRepository jobRepository,
      LocksRepository locksRepository,
      TriggerRepository triggerRepository) {
    this.persister = persister;
    this.signaler = signaler;
    this.jobRepository = jobRepository;
    this.locksRepository = locksRepository;
    this.triggerRepository = triggerRepository;
  }

  public void jobComplete(
      OperableTrigger trigger, JobDetail job, CompletedExecutionInstruction executionInstruction) {
    log.debug("Trigger completed {}", trigger.getKey());

    if (job.isPersistJobDataAfterExecution()) {
      if (job.getJobDataMap().isDirty()) {
        log.debug("Job data map dirty, will store {}", job.getKey());
        try {
          jobRepository.storeJobInMongo(job, true);
        } catch (JobPersistenceException e) {
          throw new RuntimeException(e);
        }
      }
    }

    if (job.isConcurrentExectionDisallowed()) {
      locksRepository.unlockJob(job);
    }

    try {
      process(trigger, executionInstruction);
    } catch (JobPersistenceException e) {
      throw new RuntimeException(e);
    }

    locksRepository.unlockTrigger(trigger);
  }

  private boolean isTriggerDeletionRequested(CompletedExecutionInstruction triggerInstCode) {
    return triggerInstCode == CompletedExecutionInstruction.DELETE_TRIGGER;
  }

  private void process(OperableTrigger trigger, CompletedExecutionInstruction executionInstruction)
      throws JobPersistenceException {
    // check for trigger deleted during execution...
    OperableTrigger dbTrigger = triggerRepository.getTrigger(trigger.getKey());
    if (dbTrigger != null) {
      if (isTriggerDeletionRequested(executionInstruction)) {
        if (trigger.getNextFireTime() == null) {
          // double check for possible reschedule within job
          // execution, which would cancel the need to delete...
          if (dbTrigger.getNextFireTime() == null) {
            persister.removeTrigger(trigger.getKey());
          }
        } else {
          persister.removeTrigger(trigger.getKey());
          signaler.signalSchedulingChange(0L);
        }
      } else if (executionInstruction == CompletedExecutionInstruction.SET_TRIGGER_COMPLETE) {
        // TODO: need to store state
        signaler.signalSchedulingChange(0L);
      } else if (executionInstruction == CompletedExecutionInstruction.SET_TRIGGER_ERROR) {
        // TODO: need to store state
        signaler.signalSchedulingChange(0L);
      } else if (executionInstruction == CompletedExecutionInstruction.SET_ALL_JOB_TRIGGERS_ERROR) {
        // TODO: need to store state
        signaler.signalSchedulingChange(0L);
      } else if (executionInstruction
          == CompletedExecutionInstruction.SET_ALL_JOB_TRIGGERS_COMPLETE) {
        // TODO: need to store state
        signaler.signalSchedulingChange(0L);
      }
    }
  }
}
