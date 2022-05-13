package de.suzufa.screwbox.core.log.internal;

import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.log.LogLevel;
import de.suzufa.screwbox.core.log.LoggingAdapter;

public class DefaultLog implements Log {

    private LoggingAdapter loggingAdapter = new JavaLoggingLoggingAdapter();

    @Override
    public Log info(final String message) {
        loggingAdapter.log(LogLevel.INFO, message);
        return this;
    }

    @Override
    public Log debug(final String message) {
        loggingAdapter.log(LogLevel.DEBUG, message);
        return this;
    }

    @Override
    public Log warning(final String message) {
        loggingAdapter.log(LogLevel.WARNING, message);
        return this;
    }

    @Override
    public Log error(final String message) {
        loggingAdapter.log(LogLevel.ERROR, message);
        return this;
    }

    @Override
    public Log error(final String message, final Exception e) {
        loggingAdapter.log(LogLevel.DEBUG, message + System.lineSeparator() + e.getStackTrace());
        return this;
    }

    @Override
    public Log setAdapter(final LoggingAdapter loggingAdapter) {
        this.loggingAdapter = loggingAdapter;
        return this;
    }

}
