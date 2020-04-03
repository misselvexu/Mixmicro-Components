package xyz.vopen.mixmicro.components.enhance.dbm.logger;

/**
 * Class which implements our Log interface by delegating to the Apache commons logging classes.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings("Duplicates")
public class CommonsLoggingLog implements Log {

  private final org.apache.commons.logging.Log log;

  public CommonsLoggingLog(String className) {
    this.log = org.apache.commons.logging.LogFactory.getLog(className);
  }

  @Override
  public boolean isLevelEnabled(Level level) {
    switch (level) {
      case TRACE:
        return log.isTraceEnabled();
      case DEBUG:
        return log.isDebugEnabled();
      case WARNING:
        return log.isWarnEnabled();
      case ERROR:
        return log.isErrorEnabled();
      case FATAL:
        return log.isFatalEnabled();
      case INFO:
      default:
        return log.isInfoEnabled();
    }
  }

  @Override
  public void log(Level level, String msg) {
    switch (level) {
      case TRACE:
        log.trace(msg);
        break;
      case DEBUG:
        log.debug(msg);
        break;
      case WARNING:
        log.warn(msg);
        break;
      case ERROR:
        log.error(msg);
        break;
      case FATAL:
        log.fatal(msg);
        break;
      case INFO:
      default:
        log.info(msg);
        break;
    }
  }

  @Override
  public void log(Level level, String msg, Throwable t) {
    switch (level) {
      case TRACE:
        log.trace(msg, t);
        break;
      case DEBUG:
        log.debug(msg, t);
        break;
      case WARNING:
        log.warn(msg, t);
        break;
      case ERROR:
        log.error(msg, t);
        break;
      case FATAL:
        log.fatal(msg, t);
        break;
      case INFO:
      default:
        log.info(msg, t);
        break;
    }
  }
}
