package xyz.vopen.mixmicro.components.mongo.client.logging.jdk;

import xyz.vopen.mixmicro.components.mongo.client.logging.Logger;
import xyz.vopen.mixmicro.components.mongo.client.logging.LoggerFactory;

/** A logger factory using the JDK's logging. */
public class JDKLoggerFactory implements LoggerFactory {

  @Override
  public Logger get(final Class<?> c) {
    return new JDKLogger(c);
  }
}
