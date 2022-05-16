package de.suzufa.screwbox.core.log.internal;

import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.log.LogLevel;
import de.suzufa.screwbox.core.log.LoggingAdapter;

public class DefaultLog implements Log {

    private LoggingAdapter loggingAdapter = new ConsoleLoggingAdapter();

    @Override
    public Log log(final LogLevel level, final String message) {
        loggingAdapter.log(level, message);
        return this;
    }

    @Override
    public Log info(final String message) {
        return log(LogLevel.INFO, message);
    }

    @Override
    public Log debug(final String message) {
        return log(LogLevel.DEBUG, message);
    }

    @Override
    public Log warn(final String message) {
        return log(LogLevel.WARNING, message);
    }

    @Override
    public Log error(final String message) {
        return log(LogLevel.ERROR, message);
    }

    @Override
    public Log error(final String message, final Exception e) {
        return log(LogLevel.DEBUG, message + System.lineSeparator() + e.getStackTrace());
    }

    @Override
    public Log setAdapter(final LoggingAdapter loggingAdapter) {
        this.loggingAdapter = loggingAdapter;
        return this;
    }

}
