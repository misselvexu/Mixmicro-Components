package xyz.vopen.mixmicro.components.enhance.schedule.quartz;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.quartz.SchedulerConfigException;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerSignaler;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.cluster.*;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.mongo.MongoConnector;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.mongo.MongoConnectorBuilder;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.*;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.trigger.MisfireHandler;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.trigger.TriggerConverter;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.util.Clock;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.util.ExpiryCalculator;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.util.QueryHelper;

import java.util.Properties;

public class MongoStoreAssembler {

  public MongoConnector mongoConnector;
  public JobCompleteHandler jobCompleteHandler;
  public LockManager lockManager;
  public TriggerStateManager triggerStateManager;
  public TriggerRunner triggerRunner;
  public TriggerAndJobPersister persister;

  public CalendarRepository calendarRepository;
  public JobRepository jobRepository;
  public LocksRepository locksRepository;
  public SchedulerRepository schedulerRepository;
  public PausedJobGroupsRepository pausedJobGroupsRepository;
  public PausedTriggerGroupsRepository pausedTriggerGroupsRepository;
  public TriggerRepository triggerRepository;

  public TriggerRecoverer triggerRecoverer;
  public CheckinExecutor checkinExecutor;

  private QueryHelper queryHelper = new QueryHelper();
  private TriggerConverter triggerConverter;

  public void build(
      MongoDBJobStore jobStore,
      ClassLoadHelper loadHelper,
      SchedulerSignaler signaler,
      Properties quartzProps)
      throws SchedulerConfigException, ClassNotFoundException, IllegalAccessException,
          InstantiationException {
    mongoConnector = createMongoConnector(jobStore);

    JobDataConverter jobDataConverter = new JobDataConverter(jobStore.isJobDataAsBase64());

    jobRepository = createJobDao(jobStore, loadHelper, jobDataConverter);

    triggerConverter = new TriggerConverter(jobRepository, jobDataConverter);

    triggerRepository = createTriggerDao(jobStore);
    calendarRepository = createCalendarDao(jobStore);
    locksRepository = createLocksDao(jobStore);
    pausedJobGroupsRepository = createPausedJobGroupsDao(jobStore);
    pausedTriggerGroupsRepository = createPausedTriggerGroupsDao(jobStore);
    schedulerRepository = createSchedulerDao(jobStore);

    persister = createTriggerAndJobPersister();

    jobCompleteHandler = createJobCompleteHandler(signaler);

    lockManager = createLockManager(jobStore);

    triggerStateManager = createTriggerStateManager();

    MisfireHandler misfireHandler = createMisfireHandler(jobStore, signaler);

    RecoveryTriggerFactory recoveryTriggerFactory = new RecoveryTriggerFactory(jobStore.instanceId);

    triggerRecoverer =
        new TriggerRecoverer(
            locksRepository,
            persister,
            lockManager,
            triggerRepository,
            jobRepository,
            recoveryTriggerFactory,
            misfireHandler);

    triggerRunner = createTriggerRunner(misfireHandler);

    checkinExecutor = createCheckinExecutor(jobStore, loadHelper, quartzProps);
  }

  private CheckinExecutor createCheckinExecutor(
      MongoDBJobStore jobStore, ClassLoadHelper loadHelper, Properties quartzProps)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    return new CheckinExecutor(
        createCheckinTask(jobStore, loadHelper),
        jobStore.clusterCheckinIntervalMillis,
        jobStore.instanceId);
  }

  private Runnable createCheckinTask(MongoDBJobStore jobStore, ClassLoadHelper loadHelper)
      throws ClassNotFoundException, IllegalAccessException, InstantiationException {
    Runnable errorHandler;
    Class<?> aClass;
    if (jobStore.getCheckInErrorHandler() == null) {
      // current default, see
      aClass = ErrorHandler.class;
    } else {
      aClass = loadHelper.loadClass(jobStore.getCheckInErrorHandler());
    }
    errorHandler = (Runnable) aClass.newInstance();
    return new CheckinTask(schedulerRepository, errorHandler);
  }

  private CalendarRepository createCalendarDao(MongoDBJobStore jobStore) {
    return new CalendarRepository(getCollection(jobStore, "calendars"));
  }

  private JobRepository createJobDao(
      MongoDBJobStore jobStore, ClassLoadHelper loadHelper, JobDataConverter jobDataConverter) {
    JobConverter jobConverter =
        new JobConverter(jobStore.getClassLoaderHelper(loadHelper), jobDataConverter);
    return new JobRepository(getCollection(jobStore, "jobs"), queryHelper, jobConverter);
  }

  private JobCompleteHandler createJobCompleteHandler(SchedulerSignaler signaler) {
    return new JobCompleteHandler(persister, signaler, jobRepository, locksRepository, triggerRepository);
  }

  private LocksRepository createLocksDao(MongoDBJobStore jobStore) {
    return new LocksRepository(getCollection(jobStore, "locks"), Clock.SYSTEM_CLOCK, jobStore.instanceId);
  }

  private LockManager createLockManager(MongoDBJobStore jobStore) {
    ExpiryCalculator expiryCalculator =
        new ExpiryCalculator(
            schedulerRepository,
            Clock.SYSTEM_CLOCK,
            jobStore.jobTimeoutMillis,
            jobStore.triggerTimeoutMillis,
            jobStore.isClustered());
    return new LockManager(locksRepository, expiryCalculator);
  }

  private MisfireHandler createMisfireHandler(
      MongoDBJobStore jobStore, SchedulerSignaler signaler) {
    return new MisfireHandler(calendarRepository, signaler, jobStore.misfireThreshold);
  }

  private MongoConnector createMongoConnector(MongoDBJobStore jobStore)
      throws SchedulerConfigException {
    return MongoConnectorBuilder.builder()
        .withConnector(jobStore.mongoConnector)
        .withDatabase(jobStore.mongoDatabase)
        .withClient(jobStore.mongo)
        .withUri(jobStore.mongoUri)
        .withCredentials(jobStore.username, jobStore.password)
        .withAddresses(jobStore.addresses)
        .withDatabaseName(jobStore.dbName)
        .withAuthDatabaseName(jobStore.authDbName)
        .withMaxConnections(jobStore.mongoOptionMaxConnections)
        .withConnectTimeoutMillis(jobStore.mongoOptionConnectTimeoutMillis)
        .withReadTimeoutMillis(jobStore.mongoOptionReadTimeoutMillis)
        .withSocketKeepAlive(jobStore.mongoOptionSocketKeepAlive)
        .withSSL(jobStore.mongoOptionEnableSSL, jobStore.mongoOptionSslInvalidHostNameAllowed)
        .withTrustStore(
            jobStore.mongoOptionTrustStorePath,
            jobStore.mongoOptionTrustStorePassword,
            jobStore.mongoOptionTrustStoreType)
        .withKeyStore(
            jobStore.mongoOptionKeyStorePath,
            jobStore.mongoOptionKeyStorePassword,
            jobStore.mongoOptionKeyStoreType)
        .withWriteConcernWriteTimeout(jobStore.mongoOptionWriteConcernTimeoutMillis)
        .withWriteConcernW(jobStore.mongoOptionWriteConcernW)
        .build();
  }

  private PausedJobGroupsRepository createPausedJobGroupsDao(MongoDBJobStore jobStore) {
    return new PausedJobGroupsRepository(getCollection(jobStore, "paused_job_groups"));
  }

  private PausedTriggerGroupsRepository createPausedTriggerGroupsDao(MongoDBJobStore jobStore) {
    return new PausedTriggerGroupsRepository(getCollection(jobStore, "paused_trigger_groups"));
  }

  private SchedulerRepository createSchedulerDao(MongoDBJobStore jobStore) {
    return new SchedulerRepository(
        getCollection(jobStore, "schedulers"),
        jobStore.schedulerName,
        jobStore.instanceId,
        jobStore.clusterCheckinIntervalMillis,
        Clock.SYSTEM_CLOCK);
  }

  private TriggerAndJobPersister createTriggerAndJobPersister() {
    return new TriggerAndJobPersister(triggerRepository, jobRepository, triggerConverter);
  }

  private TriggerRepository createTriggerDao(MongoDBJobStore jobStore) {
    return new TriggerRepository(getCollection(jobStore, "triggers"), queryHelper, triggerConverter);
  }

  private TriggerRunner createTriggerRunner(MisfireHandler misfireHandler) {
    return new TriggerRunner(
        persister,
        triggerRepository,
        jobRepository,
        locksRepository,
        calendarRepository,
        misfireHandler,
        triggerConverter,
        lockManager,
        triggerRecoverer);
  }

  private TriggerStateManager createTriggerStateManager() {
    return new TriggerStateManager(
        triggerRepository, jobRepository, pausedJobGroupsRepository, pausedTriggerGroupsRepository, queryHelper);
  }

  private MongoCollection<Document> getCollection(MongoDBJobStore jobStore, String name) {
    return mongoConnector.getCollection(jobStore.collectionPrefix + name);
  }
}
