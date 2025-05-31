package dev.screwbox.core.log;

import dev.screwbox.core.Engine;

/**
 * Provides some super basic logging features and the ability to pick up engine
 * log events via {@link Log#setAdapter(LoggingAdapter)}.
 * <p>
 * Without changing the {@link LoggingAdapter} the {@link ConsoleLoggingAdapter}
 * is used to log everything to console only.
 *
 * @see <a href="http://screwbox.dev/docs/core-modules/log">Documentation</a>
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
     * Logs a message using the given {@link LogLevel}. Will replace placeholder '{}' with specified parameters.
     *
     * @throws IllegalArgumentException if number of placeholders doesn't match number of parameters
     * @since 3.4.0
     */
    Log log(LogLevel level, String message, Object... parameters);

    /**
     * Logs a message using {@link LogLevel#DEBUG}. Will replace placeholder '{}' with specified parameters.
     *
     * @throws IllegalArgumentException if number of placeholders doesn't match number of parameters
     * @since 3.4.0
     */
    Log debug(String message, Object... parameters);

    /**
     * Logs a message using {@link LogLevel#INFO}. Will replace placeholder '{}' with specified parameters.
     *
     * @throws IllegalArgumentException if number of placeholders doesn't match number of parameters
     * @since 3.4.0
     */
    Log info(String message, Object... parameters);

    /**
     * Logs a message using {@link LogLevel#WARNING}. Will replace placeholder '{}' with specified parameters.
     *
     * @throws IllegalArgumentException if number of placeholders doesn't match number of parameters
     * @since 3.4.0
     */
    Log warn(String message, Object... parameters);

    /**
     * Logs a message using {@link LogLevel#ERROR}. Will replace placeholder '{}' with specified parameters.
     *
     * @throws IllegalArgumentException if number of placeholders doesn't match number of parameters
     * @since 3.4.0
     */
    Log error(String message, Object... parameters);

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
