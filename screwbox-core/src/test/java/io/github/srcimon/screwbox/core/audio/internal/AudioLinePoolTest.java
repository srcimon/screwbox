package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AudioLinePoolTest {

    private static final AudioFormat STEREO_FORMAT = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    private static final AudioFormat MONO_FORMAT = new AudioFormat(PCM_SIGNED, 44100, 16, 2, 2, 44100, false);

    @Mock
    AudioAdapter audioAdapter;

    @Spy
    AudioConfiguration configuration = new AudioConfiguration().setMaxLines(2);

    @InjectMocks
    AudioLinePool audioLinePool;

    @Test
    void size_noLinesStartet_isZero() {
        assertThat(audioLinePool.size()).isZero();
    }

    @Test
    void prepareLine_maxLinesReached_removesOldLine() {
        when(audioAdapter.createSourceLine(STEREO_FORMAT))
                .thenReturn(mock(SourceDataLine.class), mock(SourceDataLine.class), mock(SourceDataLine.class));

        audioLinePool.prepareLine(STEREO_FORMAT);
        audioLinePool.prepareLine(STEREO_FORMAT);
        audioLinePool.prepareLine(STEREO_FORMAT);

        assertThat(audioLinePool.size()).isEqualTo(2);
    }

    @Test
    void prepareLine_noLinesYet_createsLine() {
        audioLinePool.prepareLine(STEREO_FORMAT);

        assertThat(audioLinePool.size()).isOne();
    }

    @Test
    void aquireLine_lineHasBeenPrepared_returnsPreparedLine() {
        SourceDataLine stereoLine = mock(SourceDataLine.class);
        when(stereoLine.getFormat()).thenReturn(STEREO_FORMAT);
        when(audioAdapter.createSourceLine(STEREO_FORMAT)).thenReturn(stereoLine);
        audioLinePool.prepareLine(STEREO_FORMAT);

        SourceDataLine aquireLine = audioLinePool.aquireLine(STEREO_FORMAT);

        assertThat(aquireLine).isEqualTo(stereoLine);
    }

    @Test
    void aquireLine_lineHasNotBeenPrepared_returnsNewLine() {
        SourceDataLine monoLine = mock(SourceDataLine.class);
        when(audioAdapter.createSourceLine(MONO_FORMAT)).thenReturn(monoLine);

        SourceDataLine aquireLine = audioLinePool.aquireLine(MONO_FORMAT);
        assertThat(aquireLine).isEqualTo(monoLine);
    }

    @Test
    void aquireLine_stereoLineAvailableButMonoRequested_returnsNewMonoLine() {
        SourceDataLine stereoLine = mock(SourceDataLine.class);
        when(stereoLine.getFormat()).thenReturn(STEREO_FORMAT);
        when(audioAdapter.createSourceLine(STEREO_FORMAT)).thenReturn(stereoLine);
        audioLinePool.prepareLine(STEREO_FORMAT);

        SourceDataLine monoLine = mock(SourceDataLine.class);
        when(audioAdapter.createSourceLine(MONO_FORMAT)).thenReturn(monoLine);

        SourceDataLine aquireLine = audioLinePool.aquireLine(MONO_FORMAT);
        assertThat(aquireLine).isEqualTo(monoLine);
    }
}
