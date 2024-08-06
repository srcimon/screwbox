package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AudioAdapterTest {

    @Mock
    SourceDataLine line;

    @Mock
    FloatControl floatControl;

    @InjectMocks
    AudioAdapter audioAdapter;

    @Test
    void setVolume_fourtyPercent_appliesCalculatedFloatValueToLine() {
        when(line.getControl(FloatControl.Type.MASTER_GAIN)).thenReturn(floatControl);

        AudioAdapter.setVolume(line, Percent.of(0.4));

        verify(floatControl).setValue(-7.9588003f);
    }

    @Test
    void setPan_validValue_appliesValueToLine() {
        when(line.getControl(FloatControl.Type.PAN)).thenReturn(floatControl);

        AudioAdapter.setPan(line, 0.2);

        verify(floatControl).setValue(0.2f);
    }

    @Test
    void getAudioFormat_validSound_returnsFormat() {
        byte[] sound = SoundBundle.JUMP.get().content();

        var audioFormat = AudioAdapter.getAudioFormat(sound);

        assertThat(audioFormat.getFrameRate()).isEqualTo(44100.0f);
        assertThat(audioFormat.getChannels()).isEqualTo(2);
    }

    @Test
    void getAudioFormat_noSoundData_throwsException() {
        byte[] noSound = "no-sound".getBytes();

        assertThatThrownBy(() -> AudioAdapter.getAudioFormat(noSound))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("could not load audio content");
    }
}
