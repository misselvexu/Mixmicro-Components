package xyz.vopen.mixmicro.components.enhance.schedule.quartz;

import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.JobRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.PausedJobGroupsRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.PausedTriggerGroupsRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository.TriggerRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.util.GroupHelper;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.util.QueryHelper;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.util.TriggerGroupHelper;
import org.bson.types.ObjectId;
import org.quartz.JobKey;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TriggerStateManager {

  private final TriggerRepository triggerRepository;
  private final JobRepository jobRepository;
  private PausedJobGroupsRepository pausedJobGroupsRepository;
  private final PausedTriggerGroupsRepository pausedTriggerGroupsRepository;
  private final QueryHelper queryHelper;

  public TriggerStateManager(
      TriggerRepository triggerRepository,
      JobRepository jobRepository,
      PausedJobGroupsRepository pausedJobGroupsRepository,
      PausedTriggerGroupsRepository pausedTriggerGroupsRepository,
      QueryHelper queryHelper) {
    this.triggerRepository = triggerRepository;
    this.jobRepository = jobRepository;
    this.pausedJobGroupsRepository = pausedJobGroupsRepository;
    this.pausedTriggerGroupsRepository = pausedTriggerGroupsRepository;
    this.queryHelper = queryHelper;
  }

  public Set<String> getPausedTriggerGroups() {
    return pausedTriggerGroupsRepository.getPausedGroups();
  }

  public TriggerState getState(TriggerKey triggerKey) {
    return getTriggerState(triggerRepository.getState(triggerKey));
  }

  public void pause(TriggerKey triggerKey) {
    triggerRepository.setState(triggerKey, Constants.STATE_PAUSED);
  }

  public Collection<String> pause(GroupMatcher<TriggerKey> matcher) {
    triggerRepository.setStateInMatching(matcher, Constants.STATE_PAUSED);

    final GroupHelper groupHelper = new GroupHelper(triggerRepository.getCollection(), queryHelper);
    final Set<String> set = groupHelper.groupsThatMatch(matcher);
    pausedTriggerGroupsRepository.pauseGroups(set);

    return set;
  }

  public void pauseAll() {
    final GroupHelper groupHelper = new GroupHelper(triggerRepository.getCollection(), queryHelper);
    triggerRepository.setStateInAll(Constants.STATE_PAUSED);
    pausedTriggerGroupsRepository.pauseGroups(groupHelper.allGroups());
  }

  public void pauseJob(JobKey jobKey) {
    final ObjectId jobId = jobRepository.getJob(jobKey).getObjectId("_id");
    final TriggerGroupHelper groupHelper =
        new TriggerGroupHelper(triggerRepository.getCollection(), queryHelper);
    List<String> groups = groupHelper.groupsForJobId(jobId);
    triggerRepository.setStateByJobId(jobId, Constants.STATE_PAUSED);
    pausedTriggerGroupsRepository.pauseGroups(groups);
  }

  public Collection<String> pauseJobs(GroupMatcher<JobKey> groupMatcher) {
    final TriggerGroupHelper groupHelper =
        new TriggerGroupHelper(triggerRepository.getCollection(), queryHelper);
    List<String> groups = groupHelper.groupsForJobIds(jobRepository.idsOfMatching(groupMatcher));
    triggerRepository.setStateInGroups(groups, Constants.STATE_PAUSED);
    pausedJobGroupsRepository.pauseGroups(groups);
    return groups;
  }

  public void resume(TriggerKey triggerKey) {
    // TODO: port blocking behavior and misfired triggers handling from StdJDBCDelegate in Quartz
    triggerRepository.setState(triggerKey, Constants.STATE_WAITING);
  }

  public Collection<String> resume(GroupMatcher<TriggerKey> matcher) {
    triggerRepository.setStateInMatching(matcher, Constants.STATE_WAITING);

    final GroupHelper groupHelper = new GroupHelper(triggerRepository.getCollection(), queryHelper);
    final Set<String> set = groupHelper.groupsThatMatch(matcher);
    pausedTriggerGroupsRepository.unpauseGroups(set);
    return set;
  }

  public void resume(JobKey jobKey) {
    final ObjectId jobId = jobRepository.getJob(jobKey).getObjectId("_id");
    // TODO: port blocking behavior and misfired triggers handling from StdJDBCDelegate in Quartz
    triggerRepository.setStateByJobId(jobId, Constants.STATE_WAITING);
  }

  public void resumeAll() {
    final GroupHelper groupHelper = new GroupHelper(triggerRepository.getCollection(), queryHelper);
    triggerRepository.setStateInAll(Constants.STATE_WAITING);
    pausedTriggerGroupsRepository.unpauseGroups(groupHelper.allGroups());
  }

  public Collection<String> resumeJobs(GroupMatcher<JobKey> groupMatcher) {
    final TriggerGroupHelper groupHelper =
        new TriggerGroupHelper(triggerRepository.getCollection(), queryHelper);
    List<String> groups = groupHelper.groupsForJobIds(jobRepository.idsOfMatching(groupMatcher));
    triggerRepository.setStateInGroups(groups, Constants.STATE_WAITING);
    pausedJobGroupsRepository.unpauseGroups(groups);
    return groups;
  }

  public void resetTriggerFromErrorState(TriggerKey triggerKey) {
    // Atomic updates cannot be done with the current model - across collections.
    String currentState = triggerRepository.getState(triggerKey);
    if (!Constants.STATE_ERROR.equals(currentState)) {
      return;
    }
    String newState = Constants.STATE_WAITING;
    if (pausedTriggerGroupsRepository.getPausedGroups().contains(triggerKey.getGroup())) {
      newState = Constants.STATE_PAUSED;
    }
    triggerRepository.transferState(triggerKey, Constants.STATE_ERROR, newState);
  }

  private TriggerState getTriggerState(String value) {
    if (value == null) {
      return TriggerState.NONE;
    }

    if (value.equals(Constants.STATE_DELETED)) {
      return TriggerState.NONE;
    }

    if (value.equals(Constants.STATE_COMPLETE)) {
      return TriggerState.COMPLETE;
    }

    if (value.equals(Constants.STATE_PAUSED)) {
      return TriggerState.PAUSED;
    }

    if (value.equals(Constants.STATE_PAUSED_BLOCKED)) {
      return TriggerState.PAUSED;
    }

    if (value.equals(Constants.STATE_ERROR)) {
      return TriggerState.ERROR;
    }

    if (value.equals(Constants.STATE_BLOCKED)) {
      return TriggerState.BLOCKED;
    }

    // waiting or acquired
    return TriggerState.NORMAL;
  }
}
