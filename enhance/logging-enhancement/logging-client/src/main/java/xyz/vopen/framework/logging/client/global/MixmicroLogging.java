package xyz.vopen.framework.logging.client.global;

/**
 * Global log collection interface Provide debug, info, and error log collection
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface MixmicroLogging {
    /**
     * Collect Debug Level Logs
     *
     * @param msg log content
     */
    void debug(String msg);

    /**
     * Collect Debug Level Logs
     *
     * @param msg         log content
     * @param callerClass logger caller class name
     */
    void debug(String msg, String callerClass);

    /**
     * Collect Debug Level Logs
     *
     * @param format    Unformatted log content
     * @param arguments List of parameters corresponding to log content
     */
    void debug(String format, Object... arguments);

    /**
     * Collect Debug Level Logs
     *
     * @param format      Unformatted log content
     * @param callerClass logger caller class name
     * @param arguments   List of parameters corresponding to log content
     */
    void debug(String format, String callerClass, Object... arguments);

    /**
     * Collect Info Level Logs
     *
     * @param msg log content
     */
    void info(String msg);

    /**
     * Collect Info Level Logs
     *
     * @param msg         log content
     * @param callerClass logger caller class name
     */
    void info(String msg, String callerClass);

    /**
     * Collect Info Level Logs
     *
     * @param format    Unformatted log content
     * @param arguments List of parameters corresponding to log content
     */
    void info(String format, Object... arguments);

    /**
     * Collect Info Level Logs
     *
     * @param format      Unformatted log content
     * @param callerClass logger caller class name
     * @param arguments   List of parameters corresponding to log content
     */
    void info(String format, String callerClass, Object... arguments);

    /**
     * Collect Error Level Logs
     *
     * @param msg log content
     */
    void error(String msg);

    /**
     * Collect Error Level Logs
     *
     * @param msg         log content
     * @param callerClass logger caller class name
     */
    void error(String msg, String callerClass);

    /**
     * Collect Error Level Logs
     *
     * @param msg       log content
     * @param throwable Exception object instance
     */
    void error(String msg, Throwable throwable);

    /**
     * Collect Error Level Logs
     *
     * @param msg         log content
     * @param callerClass logger caller class name
     * @param throwable   Exception object instance
     */
    void error(String msg, String callerClass, Throwable throwable);

    /**
     * Collect Error Level Logs
     *
     * @param format    Unformatted log content
     * @param arguments List of parameters corresponding to log content
     */
    void error(String format, Object... arguments);

    /**
     * Collect Error Level Logs
     *
     * @param format      Unformatted log content
     * @param callerClass logger caller class name
     * @param arguments   List of parameters corresponding to log content
     */
    void error(String format, String callerClass, Object... arguments);

    /**
     * Collect Warn Level Logs
     *
     * @param msg log content
     */
    void warn(String msg);

    /**
     * Collect Warn Level Logs
     *
     * @param msg         log content
     * @param callerClass logger caller class name
     */
    void warn(String msg, String callerClass);

    /**
     * Collect Warn Level Logs
     *
     * @param format    Unformatted log content
     * @param arguments List of parameters corresponding to log content
     */
    void warn(String format, Object... arguments);

    /**
     * Collect Warn Level Logs
     *
     * @param format      Unformatted log content
     * @param callerClass logger caller class name
     * @param arguments   List of parameters corresponding to log content
     */
    void warn(String format, String callerClass, Object... arguments);
}
