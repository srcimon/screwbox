package de.suzufa.screwbox.core.log.internal;

import de.suzufa.screwbox.core.log.LogLevel;
import de.suzufa.screwbox.core.log.LoggingAdapter;

public class ConsoleLoggingAdapter implements LoggingAdapter {

    @Override
    public void log(final LogLevel level, final String message) {
        String formattedMessage = String.format("[%-6s] %s", level.name(), message);
        if (LogLevel.ERROR.equals(level)) {
            System.err.println(formattedMessage);
        } else {
            System.out.println(formattedMessage);
        }
    }

}
