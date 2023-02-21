package io.github.simonbas.screwbox.core.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConsoleLoggingAdapterTest {

    @Spy
    ConsoleLoggingAdapter consoleLoggingAdapter;

    @Captor
    ArgumentCaptor<String> message;

    @Test
    void log_levelError_logsError() {
        consoleLoggingAdapter.log(LogLevel.ERROR, "some text");

        verify(consoleLoggingAdapter).logError(message.capture());
        assertThat(message.getValue()).endsWith("[ERROR ] some text");
    }

    @Test
    void log_levelNotError_logsNormal() {
        consoleLoggingAdapter.log(LogLevel.INFO, "some text");

        verify(consoleLoggingAdapter).logNormal(message.capture());
        assertThat(message.getValue()).endsWith("[INFO  ] some text");
    }
}
