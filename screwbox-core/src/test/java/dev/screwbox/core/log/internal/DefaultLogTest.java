package dev.screwbox.core.log.internal;

import dev.screwbox.core.log.LogLevel;
import dev.screwbox.core.log.LoggingAdapter;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@MockitoSettings
class DefaultLogTest {

    @InjectMocks
    DefaultLog log;

    @Mock
    LoggingAdapter loggingAdapter;

    @Test
    void info_invokesLoggingAdapter() {
        log.info("message-text");

        verify(loggingAdapter).log(LogLevel.INFO, "message-text");
    }

    @Test
    void debug_invokesLoggingAdapter() {
        log.debug("message-text");

        verify(loggingAdapter).log(LogLevel.DEBUG, "message-text");
    }

    @Test
    void warn_invokesLoggingAdapter() {
        log.warn("message-text");

        verify(loggingAdapter).log(LogLevel.WARNING, "message-text");
    }

    @Test
    void error_invokesLoggingAdapter() {
        log.error("message-text");

        verify(loggingAdapter).log(LogLevel.ERROR, "message-text");
    }

    @Test
    void error_withStacktrace_invokesLoggingAdapter() {
        log.error(new RuntimeException("unhandled"));

        ArgumentCaptor<LogLevel> loglevelCaptor = ArgumentCaptor.forClass(LogLevel.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(loggingAdapter).log(loglevelCaptor.capture(), messageCaptor.capture());

        assertThat(loglevelCaptor.getValue()).isEqualTo(LogLevel.ERROR);
        assertThat(messageCaptor.getValue())
                .contains("java.lang.RuntimeException")
                .contains("unhandled")
                .contains("at");
    }

    @Test
    void setAdapter_changesLoggingAdapter() {
        LoggingAdapter alternativeAdapter = mock(LoggingAdapter.class);

        log.setAdapter(alternativeAdapter);
        log.log(LogLevel.INFO, "logging adapter changed");

        verify(loggingAdapter, never()).log(any(), any());
        verify(alternativeAdapter).log(LogLevel.INFO, "logging adapter changed");
    }

    @Test
    void setMinimumSeverity_levelWarning_disablesLoggingInfoAndDebug() {
        log.setMinimumSeverity(LogLevel.WARNING);
        log.info("not logged");
        log.debug("also not logged");

        verify(loggingAdapter, never()).log(any(), any());
    }

    @Test
    void setMinimumSeverity_levelWarning_allowsLoggingWarningAndError() {
        log.setMinimumSeverity(LogLevel.WARNING);
        log.warn("logged");
        log.error("also logged");

        verify(loggingAdapter).log(LogLevel.WARNING, "logged");
        verify(loggingAdapter).log(LogLevel.ERROR, "also logged");
    }

    @Test
    void setMinimumSeverity_levelNull_throwsException() {
        assertThatThrownBy(() -> log.setMinimumSeverity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("minimum level is required");
    }

    @Test
    void disable_turnsOffLogging() {
        log.disable();

        log.info("not logged");

        verify(loggingAdapter, never()).log(any(), any());
    }

    @Test
    void enable_turnsOnLogging() {
        log.disable();
        log.enable();

        log.info("logged");

        verify(loggingAdapter).log(LogLevel.INFO, "logged");
    }

    @Test
    void isActive_returnsCurrentActiveStatus() {
        assertThat(log.isActive()).isTrue();
        log.disable();
        assertThat(log.isActive()).isFalse();
    }
}
