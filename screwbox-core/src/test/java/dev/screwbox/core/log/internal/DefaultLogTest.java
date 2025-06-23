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
    void info_placeholderButNoParameter_throwsException() {
        assertThatThrownBy(() -> log.info("message-text {}"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("missing parameter value for placeholder in log message: message-text {}");
    }

    @Test
    void info_tooManyPlaceholders_throwsException() {
        assertThatThrownBy(() -> log.info("message-text {} {}", "a"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("missing parameter value for placeholder in log message: message-text a {}");
    }

    @Test
    void info_tooManyParameters_throwsException() {
        assertThatThrownBy(() -> log.info("message-text {}", "a", "b"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("missing placeholder in log message for parameter value: message-text {}");
    }

    @Test
    void info_noParameters_invokesLoggingAdapter() {
        log.info("message-text");

        verify(loggingAdapter).log(LogLevel.INFO, "message-text");
    }

    @Test
    void info_usingParameters_invokesLoggingAdapter() {
        log.info("message-text {} and a second {}", "parameter", 2);

        verify(loggingAdapter).log(LogLevel.INFO, "message-text parameter and a second 2");
    }

    @Test
    void debug_noParameters_invokesLoggingAdapter() {
        log.debug("message-text");

        verify(loggingAdapter).log(LogLevel.DEBUG, "message-text");
    }

    @Test
    void debug_usingParameters_invokesLoggingAdapter() {
        log.debug("{}{}", "a", "b");

        verify(loggingAdapter).log(LogLevel.DEBUG, "ab");
    }

    @Test
    void warn_noParameters_invokesLoggingAdapter() {
        log.warn("message-text");

        verify(loggingAdapter).log(LogLevel.WARNING, "message-text");
    }

    @Test
    void warn_usingParameters_invokesLoggingAdapter() {
        log.warn("{}message-text{}", 1, 2);

        verify(loggingAdapter).log(LogLevel.WARNING, "1message-text2");
    }

    @Test
    void error_noParameters_invokesLoggingAdapter() {
        log.error("message-text");

        verify(loggingAdapter).log(LogLevel.ERROR, "message-text");
    }

    @Test
    void error_usingParameters_invokesLoggingAdapter() {
        log.error("message-text {}", "4");

        verify(loggingAdapter).log(LogLevel.ERROR, "message-text 4");
    }

    @Test
    void error_withStacktrace_invokesLoggingAdapter() {
        log.error(new RuntimeException("unhandled"));

        ArgumentCaptor<LogLevel> logLevelCaptor = ArgumentCaptor.forClass(LogLevel.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(loggingAdapter).log(logLevelCaptor.capture(), messageCaptor.capture());

        assertThat(logLevelCaptor.getValue()).isEqualTo(LogLevel.ERROR);
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
