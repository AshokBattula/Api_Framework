package Logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

/**
 * A centralized utility class for handling application logging using Log4j2.
 * <p>
 * This class provides static methods to log messages at various levels
 * (INFO, DEBUG, WARN, ERROR, FATAL) and allows custom logging with a dynamic level.
 * <p>
 * Example usage:
 * <pre>
 *     logs.logInfoMessage(MyClass.class, "This is an info log");
 *     logs.logErrorMessage(MyClass.class, "This is an error log");
 * </pre>
 *
 * @author Ashok
 * @version 1.0
 * @since 2025-05-01
 */
public final class logs {

    /**
     * Private constructor to prevent instantiation.
     */
    private logs() {}

    /**
     * Returns the {@link Logger} instance for the given class.
     *
     * @param clazz the class for which the logger is required
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    /**
     * Logs an informational message.
     *
     * @param clazz   the class context of the log message
     * @param message the message to log
     */
    public static void logInfoMessage(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    /**
     * Logs a warning message.
     *
     * @param clazz   the class context of the log message
     * @param message the message to log
     */
    public static void logWarnMessage(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }

    /**
     * Logs an error message.
     *
     * @param clazz   the class context of the log message
     * @param message the message to log
     */
    public static void logErrorMessage(Class<?> clazz, String message) {
        getLogger(clazz).error(message);
    }

    /**
     * Logs a debug-level message.
     *
     * @param clazz   the class context of the log message
     * @param message the message to log
     */
    public static void logDebug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }

    /**
     * Logs a fatal-level message.
     *
     * @param clazz   the class context of the log message
     * @param message the message to log
     */
    public static void logFatalMessage(Class<?> clazz, String message) {
        getLogger(clazz).fatal(message);
    }

    /**
     * Logs a message with a custom {@link Level}.
     *
     * @param clazz   the class context of the log message
     * @param level   the log level to use (e.g., Level.INFO, Level.DEBUG)
     * @param message the message to log
     */
    public static void log(Class<?> clazz, Level level, String message) {
        getLogger(clazz).log(level, message);
    }
}
