package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class AudioAdapterTest {

    @Mock
    SourceDataLine line;

    @Mock
    FloatControl floatControl;

    @Test
    void setVolume_fourtyPercent_appliesCalculatedFloatValueToLine() {
        when(floatControl.getMinimum()).thenReturn(-10f);
        when(floatControl.getMaximum()).thenReturn(10f);
        when(line.getControl(FloatControl.Type.MASTER_GAIN)).thenReturn(floatControl);

        AudioAdapter.setVolume(line, Percent.of(0.4));

        verify(floatControl).setValue(-7.9588003f);
    }

    @Test
    void setPan_valueTooLow_appliesCappedValueToLine() {
        when(floatControl.getMinimum()).thenReturn(-1f);
        when(floatControl.getMaximum()).thenReturn(1f);
        when(line.getControl(FloatControl.Type.PAN)).thenReturn(floatControl);

        AudioAdapter.setPan(line, -9);

        verify(floatControl).setValue(-1f);
    }

    @Test
    void setPan_valueTooHigh_appliesCappedValueToLine() {
        when(floatControl.getMinimum()).thenReturn(-1f);
        when(floatControl.getMaximum()).thenReturn(1f);
        when(line.getControl(FloatControl.Type.PAN)).thenReturn(floatControl);

        AudioAdapter.setPan(line, 9);

        verify(floatControl).setValue(1f);
    }

    @Test
    void setPan_validValue_appliesValueToLine() {
        when(floatControl.getMinimum()).thenReturn(-1f);
        when(floatControl.getMaximum()).thenReturn(1f);
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
