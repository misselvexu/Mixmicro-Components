package xyz.vopen.mixmicro.components.enhance.mail.service.impl;


import com.google.common.base.Preconditions;
import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmailSchedulingPayload;
import xyz.vopen.mixmicro.components.enhance.mail.utils.TimeUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

/**
 * {@link PriorityQueueManager}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Slf4j
public class PriorityQueueManager implements Closeable {

  private final boolean hasPersistence;
  private volatile int currentlyQueued;
  private final TreeSet<MixmicroEmailSchedulingPayload>[] queues;
  private final int maxInMemory;
  // This is important because within the cycle length we can add an email in memory if we have room for it and is within "queuabilityDelta" time units of the last loaded.
  private final Duration queuabilityDelta;

  private final Lock queueLock = new ReentrantLock();
  private final Condition notDequeuing = queueLock.newCondition();
  private final Condition notEnqueuing = queueLock.newCondition();

  enum CurrentOperation {
    DEQUEUING, ENQUEUING, NONE, CLOSING
  }

  private final ReentrantReadWriteLock currentOperationLock = new ReentrantReadWriteLock();
  private CurrentOperation currentOperation = CurrentOperation.NONE;

  PriorityQueueManager(final int numberOfPriorityLevels,
                       final boolean hasPersistence,
                       final int maxInMemory,
                       @NonNull final Duration queuabilityDelta) {
    Preconditions.checkArgument(numberOfPriorityLevels > 0, "Number of priority levels should be a positive number, while %s was given", numberOfPriorityLevels);
    Preconditions.checkArgument(maxInMemory > 0, "Number of max emails in memory should be a positive number, while %s was given", maxInMemory);

    this.hasPersistence = hasPersistence;

    this.maxInMemory = maxInMemory;

    this.queuabilityDelta = queuabilityDelta;

    queues = new TreeSet[numberOfPriorityLevels];
    for (int i = 0; i < numberOfPriorityLevels; i++) {
      queues[i] = new TreeSet<>();
    }
  }

  public int numberOfLevels() {
    return queues.length;
  }

  public boolean hasElements() {
    return currentlyQueued > 0;
  }

  public int currentlyInQueue() {
    return currentlyQueued;
  }

  public long millisToNextEmail() {
    Optional<OffsetDateTime> minScheduledTime = getStreamOfAllFirst()
        .map(MixmicroEmailSchedulingPayload::getScheduledDateTime)
        .min(OffsetDateTime::compareTo);
    return minScheduledTime.map(offsetDateTime -> offsetDateTime.toInstant().toEpochMilli()).orElse(0L);
  }

  public boolean enqueue(final MixmicroEmailSchedulingPayload emailSchedulingData, final boolean isFromPersistenceLayer) {
    log.debug("Called Enqueue [currently queued = {}, isFromPersistenceLayer = {}]", currentlyInQueue(), isFromPersistenceLayer);
    queueLock.lock();
    try {
      while (isCurrentOperationDequeuing() || isCurrentOperationEnqueuing()) {
        notDequeuing.await();
        if (!isCurrentOperationClosing()) {
          setCurrentOperationToEnqueuing();
        }
      }
      if (isCurrentOperationNone() && !isCurrentOperationClosing()) {
        setCurrentOperationToEnqueuing();
      }

      if (isCurrentOperationEnqueuing() && !isCurrentOperationClosing()) {
        final int queueIndex = queueIndex(emailSchedulingData);
        TreeSet<MixmicroEmailSchedulingPayload> queue = queues[queueIndex];
        if (!queue.contains(emailSchedulingData)) { //It may happen when fetching from persistence layer
          final boolean isEnqueuable = isFromPersistenceLayer
              //True also if there is no persistence layer or the queues are empty
              || beforeLastLoadedFromPersistenceLayer(emailSchedulingData);
          boolean dequeueLastLoaded = isEnqueuable && !canAddOneInMemory() && hasElements();

          if (isEnqueuable) {
            queues[queueIndex].add(emailSchedulingData);
            currentlyQueued++;
          } else {
            log.debug("Email scheduling data {} not queued but should be persisted afterwards", emailSchedulingData);
          }

          if (dequeueLastLoaded) {
            int queueIndexOfLatestOfAllLast = queueIndexOfLatestOfAllLast();
            TreeSet<MixmicroEmailSchedulingPayload> queueOfLatestOfAllLast = queues[queueIndexOfLatestOfAllLast];
            queueOfLatestOfAllLast.pollLast();
            currentlyQueued--;
          }
          return isEnqueuable;
        }
      }
    } catch (InterruptedException e) {
      if (!isCurrentOperationClosing()) {
        log.error("Priority queue manager interrupted during dequeuing operation.", e);
      }
      completeEnqueue();
    }
    return false;
  }

  public void completeEnqueue() {
    try {
      if (!isCurrentOperationClosing()) {
        Preconditions.checkState(isCurrentOperationEnqueuing(),
            "Cannot complete enqueue if current operation is %s.", currentOperation);
        log.debug("Completed Enqueue [currently queued = {}]", currentlyInQueue());
        setCurrentOperationToNone();
        notEnqueuing.signal();
      }
    } finally {
      queueLock.unlock();
    }
  }

  public Optional<MixmicroEmailSchedulingPayload> dequeueNext(final Duration consumerCycle) {
    log.debug("Called Dequeue [currently queued = {}]", currentlyInQueue());
    queueLock.lock();
    try {
      while (isCurrentOperationEnqueuing() || isCurrentOperationDequeuing()) {
        notDequeuing.await();
        if (!isCurrentOperationClosing()) {
          setCurrentOperationToDequeuing();
        }
      }
      if (isCurrentOperationNone() && !isCurrentOperationClosing()) {
        setCurrentOperationToDequeuing();
      }
      if (isCurrentOperationDequeuing() && !isCurrentOperationClosing()) {
        final long now = TimeUtils.now();
        for (final TreeSet<MixmicroEmailSchedulingPayload> queue : queues) {
          if (!queue.isEmpty()) {
            final long time = queue.first().getScheduledDateTime().toInstant().toEpochMilli();
            if (time - now <= consumerCycle.toMillis()) {
              //message found!
              currentlyQueued--;
              return Optional.of(queue.pollFirst());
            }
          }
        }
      }
    } catch (InterruptedException e) {
      if (!isCurrentOperationClosing()) {
        log.error("Priority queue manager interrupted during dequeuing operation.", e);
      }
      completeDequeue();
    }

    try {
      return Optional.empty();
    } finally {
      completeDequeue();
    }
  }

  public void completeDequeue() {
    try {
      if (!isCurrentOperationClosing()) {
        Preconditions.checkState(isCurrentOperationDequeuing(),
            "Cannot complete dequeue if current operation is %s.", currentOperation);
        log.debug("Completed Dequeue [currently queued = {}]", currentlyInQueue());
        setCurrentOperationToNone();
        notDequeuing.signal();
      }
    } finally {
      queueLock.unlock();
    }
  }

  protected boolean isCurrentOperationNone() {
    return isCurrentOperation(CurrentOperation.NONE);
  }

  protected boolean isCurrentOperationClosing() {
    return isCurrentOperation(CurrentOperation.CLOSING);
  }

  protected boolean isCurrentOperationDequeuing() {
    return isCurrentOperation(CurrentOperation.DEQUEUING);
  }

  protected boolean isCurrentOperationEnqueuing() {
    return isCurrentOperation(CurrentOperation.ENQUEUING);
  }

  private boolean isCurrentOperation(CurrentOperation currentOperation) {
    currentOperationLock.readLock().lock();
    try {
      return this.currentOperation == currentOperation;
    } finally {
      currentOperationLock.readLock().unlock();
    }
  }

  protected void setCurrentOperationToEnqueuing() throws InterruptedException {
    currentOperationLock.writeLock().lock();
    if (isCurrentOperationDequeuing()) {
      notDequeuing.await();
    }
    currentOperation = CurrentOperation.ENQUEUING;
    currentOperationLock.writeLock().unlock();
  }

  protected void setCurrentOperationToDequeuing() throws InterruptedException {
    currentOperationLock.writeLock().lock();
    if (isCurrentOperationEnqueuing()) {
      notEnqueuing.await();
    }
    currentOperation = CurrentOperation.DEQUEUING;
    currentOperationLock.writeLock().unlock();
  }

  protected void setCurrentOperationToNone() {
    currentOperationLock.writeLock().lock();
    currentOperation = CurrentOperation.NONE;
    currentOperationLock.writeLock().unlock();
  }

  private int queueIndex(final MixmicroEmailSchedulingPayload emailSchedulingData) {
    return emailSchedulingData.getAssignedPriority() - 1;
  }

  private boolean canAddOneInMemory() {
    return !hasPersistence || currentlyInQueue() < maxInMemory;
  }

  private Optional<MixmicroEmailSchedulingPayload> getLeastOfAllLast() {
    return getStreamOfAllLast().min((comparing(MixmicroEmailSchedulingPayload::getScheduledDateTime)));
  }

  private Optional<MixmicroEmailSchedulingPayload> getLatestOfAllLast() {
    return getStreamOfAllLast().max((comparing(MixmicroEmailSchedulingPayload::getScheduledDateTime)));
  }

  private boolean beforeLastLoadedFromPersistenceLayer(final MixmicroEmailSchedulingPayload emailSchedulingData) {
    if (!hasPersistence || !hasElements()) {
      return true;
    }

    final MixmicroEmailSchedulingPayload least = getLeastOfAllLast().get();
    final int scheduledDateTimeComparison = emailSchedulingData.getScheduledDateTime().compareTo(least.getScheduledDateTime().plus(queuabilityDelta));
    return scheduledDateTimeComparison < 0 || (scheduledDateTimeComparison == 0 && emailSchedulingData.getAssignedPriority() < least.getAssignedPriority());
  }

  private int queueIndexOfLatestOfAllLast() {
    final Optional<MixmicroEmailSchedulingPayload> latest = getLatestOfAllLast();

    Preconditions.checkState(latest.isPresent(),
        "Should not call queueIndexOfLatestOfAllLast() if no EmailSchedulingIsInQueue");

    return latest.get().getAssignedPriority() - 1;
  }

  private Stream<MixmicroEmailSchedulingPayload> getStreamOfAllLast() {
    return Arrays.stream(queues)
        .filter(queue -> !queue.isEmpty())
        .map(TreeSet::last)
        .filter(Objects::nonNull);
  }

  private Stream<MixmicroEmailSchedulingPayload> getStreamOfAllFirst() {
    return Arrays.stream(queues)
        .filter(queue -> !queue.isEmpty())
        .map(TreeSet::last)
        .filter(Objects::nonNull);
  }

  @Override
  public void close() throws IOException {
    if (currentOperationLock.isWriteLocked()) {
      currentOperationLock.writeLock().unlock();
    }
    currentOperationLock.writeLock().lock();
    currentOperation = CurrentOperation.CLOSING;
    currentOperationLock.writeLock().unlock();
    try {
      queueLock.unlock();
    } catch (IllegalMonitorStateException e) {
    }
  }

}
