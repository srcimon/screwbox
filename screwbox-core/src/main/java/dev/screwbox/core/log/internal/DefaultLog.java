package dev.screwbox.core.log.internal;

import dev.screwbox.core.log.Log;
import dev.screwbox.core.log.LogLevel;
import dev.screwbox.core.log.LoggingAdapter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static java.util.Objects.requireNonNull;

public class DefaultLog implements Log {

    private LoggingAdapter loggingAdapter;

    private LogLevel minimumLevel = LogLevel.DEBUG;
    private boolean isActive = true;

    public DefaultLog(final LoggingAdapter loggingAdapter) {
        this.loggingAdapter = loggingAdapter;
    }

    @Override
    public Log log(final LogLevel level, final String message) {
        if (isActive && isActiveForLevel(level)) {
            loggingAdapter.log(level, message);
        }
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
    public Log setAdapter(final LoggingAdapter loggingAdapter) {
        this.loggingAdapter = loggingAdapter;
        return this;
    }

    @Override
    public Log setMinimumSeverity(final LogLevel minimumLevel) {
        this.minimumLevel = requireNonNull(minimumLevel, "minimum level is required");
        return this;
    }

    @Override
    public Log disable() {
        isActive = false;
        return this;
    }

    @Override
    public Log enable() {
        isActive = true;
        return this;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public Log error(final Throwable throwable) {
        try (final var stringWriter = new StringWriter()) {
            try (final var printWriter = new PrintWriter(stringWriter)) {
                throwable.printStackTrace(printWriter);
                return error(stringWriter.toString());
            }
        } catch (final IOException e) {
            throw new IllegalStateException("error handling failed", e);
        }
    }

    private boolean isActiveForLevel(final LogLevel level) {
        return level.ordinal() >= minimumLevel.ordinal();
    }
}
