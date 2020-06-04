package xyz.vopen.mixmicro.components.enhance.schedule.core;

public interface SchedulerClientEventListener {

  void newEvent(ClientEvent event);

  SchedulerClientEventListener NOOP =
      new SchedulerClientEventListener() {

        @Override
        public void newEvent(ClientEvent event) {}
      };
}
