package io.github.srcimon.screwbox.core.audio.internal;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@MockitoSettings
class WarmupAudioTaskTest {

    @Mock
    AudioLinePool audioLinePool;

    @InjectMocks
    WarmupAudioTask warmupAudioTask;

    @Test
    void run_preparesLinePoolWithCommonSoundFormats() {
        warmupAudioTask.run();

        verify(audioLinePool, times(2)).prepareLine(any());
    }
}
