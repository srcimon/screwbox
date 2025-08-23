package dev.screwbox.core.log.internal;

import dev.screwbox.core.log.Log;
import dev.screwbox.core.log.LogLevel;
import dev.screwbox.core.log.LoggingAdapter;
import dev.screwbox.core.utils.Validate;

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
        Validate.isFalse(() -> message.contains("{}"), "missing parameter value for placeholder in log message: " + message);
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
    public Log log(final LogLevel level, final String message, final Object... parameters) {
        final var messageBuilder = new StringBuilder();
        int minPos = 0;
        int lastIndex = 0;

        for (final var parameter : parameters) {
            minPos = message.indexOf("{}", minPos);
            if(minPos == -1) {
                throw new IllegalArgumentException("missing placeholder in log message for parameter value: " + message);
            }
            messageBuilder.append(message, lastIndex, minPos);
            messageBuilder.append(parameter);
            minPos += 2;
            lastIndex = minPos;
        }
        if (lastIndex < message.length()) {
            messageBuilder.append(message.substring(lastIndex));
        }
        final String fullMessage = messageBuilder.toString();
        return log(level, fullMessage);
    }

    @Override
    public Log debug(final String message, final Object... parameters) {
        return log(LogLevel.DEBUG, message, parameters);
    }

    @Override
    public Log info(final String message, final Object... parameters) {
        return log(LogLevel.INFO, message, parameters);
    }

    @Override
    public Log warn(final String message, final Object... parameters) {
        return log(LogLevel.WARNING, message, parameters);
    }

    @Override
    public Log error(final String message, final Object... parameters) {
        return log(LogLevel.ERROR, message, parameters);
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
