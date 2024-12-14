package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.TargetDataLine;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.test.TestUtil.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@MockitoSettings
class MicrophoneMonitorTest {

    @Mock
    AudioAdapter audioAdapter;

    @Mock
    TargetDataLine targetDataLine;

    ExecutorService executor;

    MicrophoneMonitor microphoneMonitor;

    AudioConfiguration configuration;


    @BeforeEach
    void setUp() {
        executor = Executors.newSingleThreadExecutor();
        configuration = new AudioConfiguration();
        microphoneMonitor = new MicrophoneMonitor(executor, audioAdapter, configuration);
    }

    @Test
    void isActive_afterCallingLevel_isTrue() {
        when(audioAdapter.createTargetLine(any())).thenReturn(targetDataLine);
        microphoneMonitor.level();

        assertThat(microphoneMonitor.isActive()).isTrue();
    }

    @Test
    void isActive_noCallToLevel_isFalse() {
        assertThat(microphoneMonitor.isActive()).isFalse();
    }

    @Test
    void isActive_afertIdleTimeoutReached_isFalse() {
        when(audioAdapter.createTargetLine(any())).thenReturn(targetDataLine);
        configuration.setMicrophoneIdleTimeout(Duration.ofMillis(40));
        microphoneMonitor.level();

        await(() -> !microphoneMonitor.isActive(), ofSeconds(1));
        assertThat(microphoneMonitor.isActive()).isFalse();
    }

    @Test
    void level_someMicrophoneInput_isNotZero() {
        when(audioAdapter.createTargetLine(any())).thenReturn(targetDataLine);
        when(targetDataLine.getBufferSize()).thenReturn(4);

        // sorry children!
        doAnswer(invocation -> {
            var buffer = (byte[]) invocation.getArgument(0);
            buffer[1] = 127;
            buffer[2] = 127;
            return 4;
        }).when(targetDataLine).read(new byte[4], 0, 4);

        await(() -> microphoneMonitor.level().value() > 0.4, ofSeconds(1));
        assertThat(microphoneMonitor.level().value()).isGreaterThan(0.4);
    }

    @Test
    void level_firstCall_isZero() {
        when(audioAdapter.createTargetLine(any())).thenReturn(targetDataLine);
        when(targetDataLine.getBufferSize()).thenReturn(4);

        assertThat(microphoneMonitor.level().isZero()).isTrue();
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }
}
