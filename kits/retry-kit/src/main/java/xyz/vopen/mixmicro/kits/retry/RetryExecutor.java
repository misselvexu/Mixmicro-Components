package xyz.vopen.mixmicro.kits.retry;

import xyz.vopen.mixmicro.kits.retry.exception.RetriesExhaustedException;
import xyz.vopen.mixmicro.kits.retry.exception.UnexpectedException;

import java.util.concurrent.Callable;

public interface RetryExecutor<T, S> {

  S execute(Callable<T> callable) throws RetriesExhaustedException, UnexpectedException;

  S execute(Callable<T> callable, String callName)
      throws RetriesExhaustedException, UnexpectedException;
}
