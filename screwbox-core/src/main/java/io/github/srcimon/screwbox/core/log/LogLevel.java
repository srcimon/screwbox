package io.github.srcimon.screwbox.core.log;

/**
 * Severity of a log message.
 */
public enum LogLevel {

    /**
     * Used for verbose output.
     */
    DEBUG,

    /**
     * Used for noteworthy engine events.
     */
    INFO,

    /**
     * Used when something isn't right.
     */
    WARNING,

    /**
     * Used when an error occurred that didn't kill the engine.
     */
    ERROR

}
