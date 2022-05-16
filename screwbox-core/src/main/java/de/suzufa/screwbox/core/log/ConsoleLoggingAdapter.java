package de.suzufa.screwbox.core.log;

public class ConsoleLoggingAdapter implements LoggingAdapter {

    @Override
    public void log(final LogLevel level, final String message) {
        final String formattedMessage = String.format("[%-6s] %s", level.name(), message);
        if (LogLevel.ERROR.equals(level)) {
            logError(formattedMessage);
        } else {
            logNormal(formattedMessage);
        }
    }

    protected void logNormal(final String message) {
        System.out.println(message);
    }

    protected void logError(final String message) {
        System.err.println(message);
    }

}
