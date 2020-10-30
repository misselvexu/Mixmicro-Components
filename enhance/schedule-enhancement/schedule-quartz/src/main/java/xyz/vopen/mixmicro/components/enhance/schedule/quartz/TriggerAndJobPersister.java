package xyz.vopen.mixmicro.components.enhance.schedule.quartz;

import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.JobRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.TriggerRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.trigger.TriggerConverter;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.util.Keys;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobPersistenceException;
import org.quartz.TriggerKey;
import org.quartz.spi.OperableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TriggerAndJobPersister {

  private static final Logger log = LoggerFactory.getLogger(TriggerAndJobPersister.class);

  private final TriggerRepository triggerRepository;
  private final JobRepository jobRepository;
  private TriggerConverter triggerConverter;

  public TriggerAndJobPersister(
      TriggerRepository triggerRepository, JobRepository jobRepository, TriggerConverter triggerConverter) {
    this.triggerRepository = triggerRepository;
    this.jobRepository = jobRepository;
    this.triggerConverter = triggerConverter;
  }

  public List<OperableTrigger> getTriggersForJob(JobKey jobKey) throws JobPersistenceException {
    final Document doc = jobRepository.getJob(jobKey);
    return triggerRepository.getTriggersForJob(doc);
  }

  public boolean removeJob(JobKey jobKey) {
    Bson keyObject = Keys.toFilter(jobKey);
    Document item = jobRepository.getJob(keyObject);
    if (item != null) {
      jobRepository.remove(keyObject);
      triggerRepository.removeByJobId(item.get("_id"));
      return true;
    }
    return false;
  }

  public boolean removeJobs(List<JobKey> jobKeys) throws JobPersistenceException {
    for (JobKey key : jobKeys) {
      removeJob(key);
    }
    return false;
  }

  public boolean removeTrigger(TriggerKey triggerKey) {
    Bson filter = Keys.toFilter(triggerKey);
    Document trigger = triggerRepository.findTrigger(filter);
    if (trigger != null) {
      removeOrphanedJob(trigger);
      // TODO: check if can .deleteOne(filter) here
      triggerRepository.remove(filter);
      return true;
    }
    return false;
  }

  public boolean removeTriggers(List<TriggerKey> triggerKeys) throws JobPersistenceException {
    // FIXME return boolean allFound = true when all removed
    for (TriggerKey key : triggerKeys) {
      removeTrigger(key);
    }
    return false;
  }

  public boolean removeTriggerWithoutNextFireTime(OperableTrigger trigger) {
    if (trigger.getNextFireTime() == null) {
      log.info("Removing trigger {} as it has no next fire time.", trigger.getKey());
      removeTrigger(trigger.getKey());
      return true;
    }
    return false;
  }

  public boolean replaceTrigger(TriggerKey triggerKey, OperableTrigger newTrigger)
      throws JobPersistenceException {
    OperableTrigger oldTrigger = triggerRepository.getTrigger(triggerKey);
    if (oldTrigger == null) {
      return false;
    }

    if (!oldTrigger.getJobKey().equals(newTrigger.getJobKey())) {
      throw new JobPersistenceException(
          "New trigger is not related to the same job as the old trigger.");
    }

    removeOldTrigger(triggerKey);
    copyOldJobDataMap(newTrigger, oldTrigger);
    storeNewTrigger(newTrigger, oldTrigger);

    return true;
  }

  public void storeJobAndTrigger(JobDetail newJob, OperableTrigger newTrigger)
      throws JobPersistenceException {
    ObjectId jobId = jobRepository.storeJobInMongo(newJob, false);

    log.debug("Storing job {} and trigger {}", newJob.getKey(), newTrigger.getKey());
    storeTrigger(newTrigger, jobId, false);
  }

  public void storeTrigger(OperableTrigger newTrigger, boolean replaceExisting)
      throws JobPersistenceException {
    if (newTrigger.getJobKey() == null) {
      throw new JobPersistenceException(
          "Trigger must be associated with a job. Please specify a JobKey.");
    }

    Document doc = jobRepository.getJob(Keys.toFilter(newTrigger.getJobKey()));
    if (doc != null) {
      storeTrigger(newTrigger, doc.getObjectId("_id"), replaceExisting);
    } else {
      throw new JobPersistenceException("Could not find job with key " + newTrigger.getJobKey());
    }
  }

  private void copyOldJobDataMap(OperableTrigger newTrigger, OperableTrigger trigger) {
    // Copy across the job data map from the old trigger to the new one.
    newTrigger.getJobDataMap().putAll(trigger.getJobDataMap());
  }

  private boolean isNotDurable(Document job) {
    return !job.containsKey(JobConverter.JOB_DURABILITY)
        || job.get(JobConverter.JOB_DURABILITY).toString().equals("false");
  }

  private boolean isOrphan(Document job) {
    return (job != null) && isNotDurable(job) && triggerRepository.hasLastTrigger(job);
  }

  private void removeOldTrigger(TriggerKey triggerKey) {
    // Can't call remove trigger as if the job is not durable, it will remove the job too
    triggerRepository.remove(triggerKey);
  }

  // If the removal of the Trigger results in an 'orphaned' Job that is not 'durable',
  // then the job should be removed also.
  private void removeOrphanedJob(Document trigger) {
    if (trigger.containsKey(Constants.TRIGGER_JOB_ID)) {
      // There is only 1 job per trigger so no need to look further.
      Document job = jobRepository.getById(trigger.get(Constants.TRIGGER_JOB_ID));
      if (isOrphan(job)) {
        jobRepository.remove(job);
      }
    } else {
      log.debug("The trigger had no associated jobs");
    }
  }

  private void storeNewTrigger(OperableTrigger newTrigger, OperableTrigger oldTrigger)
      throws JobPersistenceException {
    try {
      storeTrigger(newTrigger, false);
    } catch (JobPersistenceException jpe) {
      storeTrigger(oldTrigger, false);
      throw jpe;
    }
  }

  private void storeTrigger(OperableTrigger newTrigger, ObjectId jobId, boolean replaceExisting)
      throws JobPersistenceException {
    Document trigger = triggerConverter.toDocument(newTrigger, jobId);
    if (replaceExisting) {
      trigger.remove("_id");
      triggerRepository.replace(newTrigger.getKey(), trigger);
    } else {
      triggerRepository.insert(trigger, newTrigger);
    }
  }
}
