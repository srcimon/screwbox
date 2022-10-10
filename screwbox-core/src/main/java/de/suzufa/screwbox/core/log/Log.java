package de.suzufa.screwbox.core.log;

import de.suzufa.screwbox.core.Engine;

/**
 * Provides some super basic logging features and the ability to pick up engine
 * log events via {@link Log#setAdapter(log.LoggingAdapter)}.
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
     * Logs a message using {@link LogLevel#ERROR} containing the stacktrace of the
     * given {@link Throwable}.
     */
    Log error(Throwable throwable);

    /**
     * Sets the {@link LoggingAdapter} that is used for logging all messages. Can be
     * used to attach the {@link Engine} logging events to your preferred logging
     * system.
     * 
     * @see ConsoleLoggingAdapter
     */
    Log setAdapter(LoggingAdapter adapter);

    /**
     * Disables all logging below the given {@link LogLevel} (not included).
     */
    Log setMinimumSeverity(LogLevel minimumLevel);

    /**
     * Turns off logging completely.
     * 
     * @see Log#setMinimumSeverity(LogLevel)
     * @see Log#enable()
     */
    Log disable();

    /**
     * Turns on logging again.
     * 
     * @see Log#setMinimumSeverity(LogLevel)
     * @see Log#disable()
     */
    Log enable();

    /**
     * Checks if logging is currently active.
     * 
     * @see Log#enable()
     * @see Log#disable()
     */
    boolean isActive();

}
