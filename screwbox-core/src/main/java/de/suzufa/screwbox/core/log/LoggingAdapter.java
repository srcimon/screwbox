package de.suzufa.screwbox.core.log;

import de.suzufa.screwbox.core.Engine;

/**
 * Adapts the {@link Engine} logging events to any logging system.
 * 
 * @see Log#setAdapter(LoggingAdapter)
 */
public interface LoggingAdapter {

    /**
     * Is invoked whenever a log message should be created.
     */
    void log(LogLevel level, String message);
}
