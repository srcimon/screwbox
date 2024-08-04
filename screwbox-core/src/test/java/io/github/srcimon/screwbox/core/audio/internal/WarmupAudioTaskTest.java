package io.github.srcimon.screwbox.core.audio.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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
