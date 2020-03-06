package xyz.vopen.mixmicro.components.enhance.cache;

import xyz.vopen.mixmicro.components.enhance.cache.anno.CacheConsts;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheResult {

    public static final String MSG_ILLEGAL_ARGUMENT = "illegal argument";

    private static Duration DEFAULT_TIMEOUT = CacheConsts.ASYNC_RESULT_TIMEOUT;

    public static final CacheResult SUCCESS_WITHOUT_MSG = new CacheResult(CacheResultCode.SUCCESS, null);
    public static final CacheResult PART_SUCCESS_WITHOUT_MSG = new CacheResult(CacheResultCode.PART_SUCCESS, null);
    public static final CacheResult FAIL_WITHOUT_MSG = new CacheResult(CacheResultCode.FAIL, null);
    public static final CacheResult FAIL_ILLEGAL_ARGUMENT = new CacheResult(CacheResultCode.FAIL, MSG_ILLEGAL_ARGUMENT);
    public static final CacheResult EXISTS_WITHOUT_MSG = new CacheResult(CacheResultCode.EXISTS, null);

    private CacheResultCode resultCode;
    private String message;
    private CompletionStage<ResultData> future;

    private Duration timeout = DEFAULT_TIMEOUT;

    public CacheResult(CompletionStage<ResultData> future) {
        this.future = future;
    }

    public CacheResult(CacheResultCode resultCode, String message) {
        this(CompletableFuture.completedFuture(new ResultData(resultCode, message, null)));
    }

    public CacheResult(Throwable ex) {
        future = CompletableFuture.completedFuture(new ResultData(ex));
    }

    public boolean isSuccess() {
        return getResultCode() == CacheResultCode.SUCCESS;
    }

    protected void waitForResult() {
        waitForResult(timeout);
    }

    public void waitForResult(Duration timeout) {
        if (resultCode != null) {
            return;
        }
        try {
            ResultData resultData = future.toCompletableFuture().get(
                    timeout.toMillis(), TimeUnit.MILLISECONDS);
            fetchResultSuccess(resultData);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            fetchResultFail(e);
        }
    }

    protected void fetchResultSuccess(ResultData resultData) {
        resultCode = resultData.getResultCode();
        message = resultData.getMessage();
    }

    protected void fetchResultFail(Throwable e) {
        resultCode = CacheResultCode.FAIL;
        message = e.getClass() + ":" + e.getMessage();
    }

    public CacheResultCode getResultCode() {
        waitForResult();
        return resultCode;
    }

    public String getMessage() {
        waitForResult();
        return message;
    }

    public CompletionStage<ResultData> future() {
        return future;
    }

    public static void setDefaultTimeout(Duration defaultTimeout) {
        DEFAULT_TIMEOUT = defaultTimeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }
}
