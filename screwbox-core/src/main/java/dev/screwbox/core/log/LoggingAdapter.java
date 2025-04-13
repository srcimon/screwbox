package dev.screwbox.core.log;

import dev.screwbox.core.Engine;

/**
 * Attaches the {@link Engine} logging events your preferred logging system.
 * 
 * @see Log#setAdapter(LoggingAdapter)
 */
public interface LoggingAdapter {

    /**
     * Is invoked whenever a log message should be created.
     */
    void log(LogLevel level, String message);
}
