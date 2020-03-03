package xyz.vopen.mixmicro.kits.retry.listener;

import xyz.vopen.mixmicro.kits.retry.Status;

public interface RetryListener<T> {

  void onEvent(Status<T> status);
}
