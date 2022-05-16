package de.suzufa.screwbox.core.log;

/**
 * Provides some basic logging features and the ability to pick up engine log
 * events via {@link Log#setAdapter(log.LoggingAdapter)}.
 * 
 * Without changing the {@link LoggingAdapter} the {@link ConsoleLoggingAdapter}
 * is used to log everything to console only.
 */
public interface Log {

    /**
     * Logs a message using the given {@link LogLevel}.
     */
    Log log(LogLevel level, String message);

    /**
     * Logs a message using {@link LogLevel#DEBUG}.
     */
    Log debug(String message);

    /**
     * Logs a message using {@link LogLevel#INFO}.
     */
    Log info(String message);

    /**
     * Logs a message using {@link LogLevel#WARNING}.
     */
    Log warn(String message);

    /**
     * Logs a message using {@link LogLevel#ERROR}.
     */
    Log error(String message);

    /**
     * Sets the {@link LoggingAdapter} that is used for logging all messages. Can be
     * used to pick up all engine log message.
     * 
     * @see ConsoleLoggingAdapter
     */
    Log setAdapter(LoggingAdapter adapter);
}
