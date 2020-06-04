package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface SchedulerName {

  String getName();

  class Fixed implements SchedulerName {
    private final String name;

    public Fixed(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }
  }

  class Hostname implements SchedulerName {
    private static final Logger LOG = LoggerFactory.getLogger(Hostname.class);
    private String cachedHostname;

    public Hostname() {
      try {
        long start = System.currentTimeMillis();
        LOG.debug("Resolving hostname..");
        cachedHostname = InetAddress.getLocalHost().getHostName();
        LOG.debug("Resolved hostname..");
        long duration = System.currentTimeMillis() - start;
        if (duration > 1000) {
          LOG.warn("Hostname-lookup took {}ms", duration);
        }
      } catch (UnknownHostException e) {
        LOG.warn("Failed to resolve hostname. Using dummy-name for scheduler.");
        cachedHostname = "failed.hostname.lookup";
      }
    }

    @Override
    public String getName() {
      return cachedHostname;
    }
  }
}
