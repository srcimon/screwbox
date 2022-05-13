package de.suzufa.screwbox.core.log.internal;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.suzufa.screwbox.core.log.LogLevel;
import de.suzufa.screwbox.core.log.LoggingAdapter;

public class JavaLoggingLoggingAdapter implements LoggingAdapter {

    private static final Logger logger = Logger.getLogger("ScrewBox");

    private static final Map<LogLevel, Level> LEVELS = Map.of(
            LogLevel.DEBUG, Level.FINE,
            LogLevel.INFO, Level.INFO,
            LogLevel.WARNING, Level.WARNING,
            LogLevel.ERROR, Level.SEVERE);

    @Override
    public void log(final LogLevel level, final String message) {
        logger.log(LEVELS.get(level), message);
    }

}
