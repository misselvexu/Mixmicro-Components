package xyz.vopen.mixmicro.components.enhance.schedule.quartz.cluster;

import com.mongodb.MongoException;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.SchedulerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The responsibility of this class is to check-in inside Scheduler Cluster. */
public class CheckinTask implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(CheckinTask.class);

  private SchedulerRepository schedulerRepository;
  private Runnable errorhandler;

  public CheckinTask(SchedulerRepository schedulerRepository, Runnable errorHandler) {
    this.schedulerRepository = schedulerRepository;
    this.errorhandler = errorHandler;
  }

  // for tests only
  public void setErrorHandler(Runnable errorHandler) {
    this.errorhandler = errorHandler;
  }

  @Override
  public void run() {
    log.debug("Node {}:{} checks-in.", schedulerRepository.schedulerName, schedulerRepository.instanceId);
    try {
      schedulerRepository.checkIn();
    } catch (MongoException e) {
      log.error("Node " + schedulerRepository.instanceId + " could not check-in: " + e.getMessage(), e);
      errorhandler.run();
    }
  }
}
