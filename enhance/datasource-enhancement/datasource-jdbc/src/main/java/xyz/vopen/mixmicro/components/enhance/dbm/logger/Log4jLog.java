package xyz.vopen.mixmicro.components.enhance.dbm.logger;

/**
 * Class which implements our Log interface by delegating to Apache Log4j.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class Log4jLog implements Log {

  private final org.apache.log4j.Logger logger;

  public Log4jLog(String className) {
    this.logger = org.apache.log4j.Logger.getLogger(className);
  }

  @Override
  public boolean isLevelEnabled(Level level) {
    return logger.isEnabledFor(levelToLog4jLevel(level));
  }

  @Override
  public void log(Level level, String msg) {
    logger.log(levelToLog4jLevel(level), msg);
  }

  @Override
  public void log(Level level, String msg, Throwable t) {
    logger.log(levelToLog4jLevel(level), msg, t);
  }

  private org.apache.log4j.Level levelToLog4jLevel(Log.Level level) {
    switch (level) {
      case TRACE:
        return org.apache.log4j.Level.TRACE;
      case DEBUG:
        return org.apache.log4j.Level.DEBUG;
      case WARNING:
        return org.apache.log4j.Level.WARN;
      case ERROR:
        return org.apache.log4j.Level.ERROR;
      case FATAL:
        return org.apache.log4j.Level.FATAL;
      case INFO:
      default:
        return org.apache.log4j.Level.INFO;
    }
  }
}
