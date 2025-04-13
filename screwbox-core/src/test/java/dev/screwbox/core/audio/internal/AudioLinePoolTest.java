package dev.screwbox.core.audio.internal;

import dev.screwbox.core.audio.AudioConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
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
    void size_noLinesStarted_isZero() {
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
        when(audioAdapter.createSourceLine(STEREO_FORMAT)).thenReturn(mock(SourceDataLine.class));

        audioLinePool.prepareLine(STEREO_FORMAT);

        assertThat(audioLinePool.size()).isOne();
    }

    @Test
    void acquireLine_lineHasBeenPrepared_returnsPreparedLine() {
        SourceDataLine stereoLine = mock(SourceDataLine.class);
        when(stereoLine.getFormat()).thenReturn(STEREO_FORMAT);
        when(audioAdapter.createSourceLine(STEREO_FORMAT)).thenReturn(stereoLine);
        audioLinePool.prepareLine(STEREO_FORMAT);

        SourceDataLine acquireLine = audioLinePool.acquireLine(STEREO_FORMAT);

        assertThat(acquireLine).isEqualTo(stereoLine);
    }

    @Test
    void acquireLine_lineHasNotBeenPrepared_returnsNewLine() {
        SourceDataLine monoLine = mock(SourceDataLine.class);
        when(audioAdapter.createSourceLine(MONO_FORMAT)).thenReturn(monoLine);

        SourceDataLine acquireLine = audioLinePool.acquireLine(MONO_FORMAT);
        assertThat(acquireLine).isEqualTo(monoLine);
    }

    @Test
    void acquireLine_stereoLineAvailableButMonoRequested_returnsNewMonoLine() {
        SourceDataLine stereoLine = mock(SourceDataLine.class);
        when(stereoLine.getFormat()).thenReturn(STEREO_FORMAT);
        when(audioAdapter.createSourceLine(STEREO_FORMAT)).thenReturn(stereoLine);
        audioLinePool.prepareLine(STEREO_FORMAT);

        SourceDataLine monoLine = mock(SourceDataLine.class);
        when(audioAdapter.createSourceLine(MONO_FORMAT)).thenReturn(monoLine);

        SourceDataLine acquireLine = audioLinePool.acquireLine(MONO_FORMAT);
        assertThat(acquireLine).isEqualTo(monoLine);
    }

    @Test
    void acquireLine_noMoreLinesCanBeAcquired_throwsException() {
        SourceDataLine line1 = mock(SourceDataLine.class);
        SourceDataLine line2 = mock(SourceDataLine.class);

        when(audioAdapter.createSourceLine(MONO_FORMAT)).thenReturn(line1, line2);

        audioLinePool.acquireLine(MONO_FORMAT);
        audioLinePool.acquireLine(MONO_FORMAT);
        assertThatThrownBy(() -> audioLinePool.acquireLine(MONO_FORMAT))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("audio line pool has reached max capacity of 2 lines");
    }

    @Test
    void acquireLine_capacityReachedButOneLineCanBeCleared_returnsClearedLine() {
        SourceDataLine line1 = mock(SourceDataLine.class);
        SourceDataLine line2 = mock(SourceDataLine.class);
        when(line2.getFormat()).thenReturn(MONO_FORMAT);

        when(audioAdapter.createSourceLine(MONO_FORMAT)).thenReturn(line1, line2);

        audioLinePool.acquireLine(MONO_FORMAT);
        audioLinePool.acquireLine(MONO_FORMAT);
        audioLinePool.releaseLine(line2);

        assertThat(audioLinePool.acquireLine(MONO_FORMAT)).isEqualTo(line2);
    }

    @Test
    void lines_linesWereCreated_returnsCreatedLines() {
        SourceDataLine line1 = mock(SourceDataLine.class);
        SourceDataLine line2 = mock(SourceDataLine.class);
        when(audioAdapter.createSourceLine(MONO_FORMAT)).thenReturn(line1, line2);

        audioLinePool.acquireLine(MONO_FORMAT);
        audioLinePool.acquireLine(MONO_FORMAT);

        assertThat(audioLinePool.lines()).containsExactlyInAnyOrder(line1, line2);
    }
}
