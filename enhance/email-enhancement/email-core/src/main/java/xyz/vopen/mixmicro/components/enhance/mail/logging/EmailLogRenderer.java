package xyz.vopen.mixmicro.components.enhance.mail.logging;

import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;
import org.slf4j.Logger;

public interface EmailLogRenderer {

  /**
   * Register a logger to be used in alternative to the one from the class.
   * <p>
   *
   * @param logger The logger to be used.
   */
  EmailLogRenderer registerLogger(Logger logger);

  /**
   * Log a trace message.
   * <p>
   * If the logger is currently enabled for the trace message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   *
   * @param message       The string message (or a key in the message catalog)
   * @param email         Email to be logged
   * @param messageParams Additional objects to be injected in the message template
   */
  void trace(String message, MixmicroEmail email, Object... messageParams);

  /**
   * Log a trace message representing the given email.
   * <p>
   * If the logger is currently enabled for the trace message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   *
   * @param email Email to be logged
   */
  void trace(MixmicroEmail email);

  /**
   * Log a debug message.
   * <p>
   * If the logger is currently enabled for the debug message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   *
   * @param message       The string message (or a key in the message catalog)
   * @param email         Email to be logged
   * @param messageParams Additional objects to be injected in the message template
   */
  void debug(String message, MixmicroEmail email, Object... messageParams);

  /**
   * Log a debug message representing the given email.
   * <p>
   * If the logger is currently enabled for the debug message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   *
   * @param email Email to be logged
   */
  void debug(MixmicroEmail email);

  /**
   * Log an info message.
   * <p>
   * If the logger is currently enabled for the info message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   *
   * @param message       The string message (or a key in the message catalog)
   * @param email         Email to be logged
   * @param messageParams Additional objects to be injected in the message template
   */
  void info(String message, MixmicroEmail email, Object... messageParams);

  /**
   * Log an info message representing the given email.
   * <p>
   * If the logger is currently enabled for the info message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   *
   * @param email Email to be logged
   */
  void info(MixmicroEmail email);

  /**
   * Log a warning message.
   * <p>
   * If the logger is currently enabled for the warning message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   *
   * @param message       The string message (or a key in the message catalog)
   * @param email         Email to be logged
   * @param messageParams Additional objects to be injected in the message template
   */
  void warn(String message, MixmicroEmail email, Object... messageParams);

  /**
   * Log a warning message representing the given email.
   * <p>
   * If the logger is currently enabled for the warning message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   *
   * @param email Email to be logged
   */
  void warn(MixmicroEmail email);

  /**
   * Log a error message.
   * <p>
   * If the logger is currently enabled for the error message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   *
   * @param message       The string message (or a key in the message catalog)
   * @param email         Email to be logged
   * @param messageParams Additional objects to be injected in the message template
   */
  void error(String message, MixmicroEmail email, Object... messageParams);

  /**
   * Log an error message representing the given email.
   * <p>
   * If the logger is currently enabled for the error message
   * level then the given message is forwarded to all the
   * registered output Handler objects.
   * <p>
   *
   * @param email Email to be logged
   */
  void error(MixmicroEmail email);

}