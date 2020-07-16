package xyz.vopen.mixmicro.components.enhance.mail.service;

import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmailSchedulingPayload;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

public interface PersistenceService {

  /**
   * Persist the {@linkplain MixmicroEmailSchedulingPayload} into the persistence service. The key used to store the
   * instance if the one returned by {@linkplain MixmicroEmailSchedulingPayload#getId()}.
   * <p>
   * Observe that in case of duplicate keys, the new values is used to override
   * the oldest without any warning.
   * </p>
   *
   * @param emailSchedulingData the data to be stored.
   */
  @Transactional(rollbackFor = Exception.class)
  void add(MixmicroEmailSchedulingPayload emailSchedulingData);

  /**
   * Retrieve an {@linkplain Optional} containing the {@linkplain MixmicroEmailSchedulingPayload} stored for the
   * given emailSchedulingDataId, if the key exists and a value was found.
   * <p>In case of no value forund the the given key, an empty {@linkplain Optional} is returned.</p>
   *
   * @param id the emailSchedulingDataId used to store an {@linkplain MixmicroEmailSchedulingPayload}.
   * @return an optional containing the scheduling data of an email if any was found for the
   * given emailSchedulingDataId; otherwise, an empty optional.
   */
  @Transactional(readOnly = true)
  Optional<MixmicroEmailSchedulingPayload> get(String id);

  /**
   * Remove the {@linkplain MixmicroEmailSchedulingPayload} associated to the given id, returning {@code true}
   * if the key was found in the persistence layer, {@code false} otherwise.
   *
   * @param id the id of stored {@linkplain MixmicroEmailSchedulingPayload}.
   * @return {@code true} if the id exists and the value was successfully removed; {@code false} otherwise.
   */
  @Transactional(rollbackFor = Exception.class)
  boolean remove(String id);

  /**
   * Persist all the {@linkplain MixmicroEmailSchedulingPayload} contained in the given
   * collection into the persistence service.  The key used to store each the
   * instance if the one returned by {@linkplain MixmicroEmailSchedulingPayload#getId()}.
   * <p>
   * Observe that in case of duplicate keys, the new values is used to override
   * the oldest without any warning.
   * </p>
   *
   * @param emailSchedulingDataList collection of {@linkplain MixmicroEmailSchedulingPayload} to be persisted
   */
  @Transactional(rollbackFor = Exception.class)
  void addAll(Collection<MixmicroEmailSchedulingPayload> emailSchedulingDataList);

  /**
   * Retrieves a batch of {@linkplain MixmicroEmailSchedulingPayload} from the persistence layer, such that
   * up to {@code batchMaxSize} instances are returned from a specific priority level.
   * <p>
   * If no data is persisted, an empty collection is returned.
   * If less than {@code batchMaxSize} are persisted, {@code batchMaxSize} are returned.
   * </p>
   *
   * @param priorityLevel the priority level from which we want to extract the batch.
   * @param batchMaxSize  the desired size of the batch.
   * @return a batch of {@linkplain MixmicroEmailSchedulingPayload} to be retrieved from the persistence layer.
   */
  @Transactional(readOnly = true)
  Collection<MixmicroEmailSchedulingPayload> getNextBatch(int priorityLevel, int batchMaxSize);


  /**
   * Retrieves a batch of {@linkplain MixmicroEmailSchedulingPayload} from the persistence layer, such that
   * up to {@code batchMaxSize} instances are returned from a whatsoever priority level.
   * <p>
   * If no data is persisted, an empty collection is returned.
   * If less than {@code batchMaxSize} are persisted, {@code batchMaxSize} are returned.
   * </p>
   *
   * @param batchMaxSize the desired size of the batch.
   * @return a batch of {@linkplain MixmicroEmailSchedulingPayload} to be retrieved from the persistence layer.
   */
  @Transactional(readOnly = true)
  Collection<MixmicroEmailSchedulingPayload> getNextBatch(int batchMaxSize);

  /**
   * Remove all the {@linkplain MixmicroEmailSchedulingPayload} in the peristence layer.
   */
  @Transactional(rollbackFor = Exception.class)
  void removeAll();

  /**
   * Remove all the {@linkplain MixmicroEmailSchedulingPayload} associated to the given priority level.
   *
   * @param priorityLevel the id of the prioroty level that has to be erased.
   */
  @Transactional(rollbackFor = Exception.class)
  void removeAll(int priorityLevel);


  /**
   * Remove all the {@linkplain MixmicroEmailSchedulingPayload} associated to the ids in the given collection.
   * If for a specific id no key or value was found in the persistence layer, then the remove
   * operation for that id is simply skipped.
   *
   * @param ids a collection of ids of stored {@linkplain MixmicroEmailSchedulingPayload}.
   */
  @Transactional(rollbackFor = Exception.class)
  void removeAll(Collection<String> ids);

}