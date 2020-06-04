package xyz.vopen.mixmicro.components.enhance.schedule.core;

public interface SchedulerState {

  boolean isShuttingDown();

  boolean isStarted();

  class SettableSchedulerState implements SchedulerState {

    private boolean isShuttingDown;
    private boolean isStarted;

    @Override
    public boolean isShuttingDown() {
      return isShuttingDown;
    }

    @Override
    public boolean isStarted() {
      return isStarted;
    }

    public void setIsShuttingDown() {
      this.isShuttingDown = true;
    }

    public void setStarted() {
      this.isStarted = true;
    }
  }
}
