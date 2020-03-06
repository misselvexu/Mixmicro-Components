package xyz.vopen.mixmicro.components.enhance.cache;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheGetResult<V> extends CacheResult {
  public static final CacheGetResult NOT_EXISTS_WITHOUT_MSG =
      new CacheGetResult(CacheResultCode.NOT_EXISTS, null, null);
  public static final CacheGetResult EXPIRED_WITHOUT_MSG =
      new CacheGetResult(CacheResultCode.EXPIRED, null, null);
  private V value;
  private CacheValueHolder<V> holder;

  public CacheGetResult(CacheResultCode resultCode, String message, CacheValueHolder<V> holder) {
    super(CompletableFuture.completedFuture(new ResultData(resultCode, message, holder)));
  }

  public CacheGetResult(CompletionStage<ResultData> future) {
    super(future);
  }

  public CacheGetResult(Throwable ex) {
    super(ex);
  }

  static Object unwrapValue(Object holder) {
    // if @Cached or @CacheCache change type from REMOTE to BOTH (or from BOTH to REMOTE),
    // during the dev/publish process, the value type which different application server put into
    // cache server will be different
    // (CacheValueHolder<V> and CacheValueHolder<CacheValueHolder<V>>, respectively).
    // So we need correct the problem at here and in MultiLevelCache.unwrapHolder
    Object v = holder;
    while (v != null && v instanceof CacheValueHolder) {
      v = ((CacheValueHolder) v).getValue();
    }
    return v;
  }

  public V getValue() {
    waitForResult();
    return value;
  }

  @Override
  protected void fetchResultSuccess(ResultData resultData) {
    super.fetchResultSuccess(resultData);
    holder = (CacheValueHolder<V>) resultData.getOriginData();
    value = (V) unwrapValue(holder);
  }

  @Override
  protected void fetchResultFail(Throwable e) {
    super.fetchResultFail(e);
    value = null;
  }

  protected CacheValueHolder<V> getHolder() {
    waitForResult();
    return holder;
  }
}
