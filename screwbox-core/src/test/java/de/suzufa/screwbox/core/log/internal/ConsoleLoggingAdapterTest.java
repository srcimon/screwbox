package de.suzufa.screwbox.core.log.internal;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.log.LogLevel;

@ExtendWith(MockitoExtension.class)
class ConsoleLoggingAdapterTest {

    @Spy
    ConsoleLoggingAdapter consoleLoggingAdapter;

    @Test
    void log_levelError_logsError() {
        consoleLoggingAdapter.log(LogLevel.ERROR, "some text");

        verify(consoleLoggingAdapter).logError("[ERROR ] some text");
    }

    @Test
    void log_levelNotError_logsNormal() {
        consoleLoggingAdapter.log(LogLevel.INFO, "some text");

        verify(consoleLoggingAdapter).logNormal("[INFO  ] some text");
    }
}
