package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

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

        audioAdapter.setVolume(line, Percent.of(0.4));

        verify(floatControl).setValue(-7.9588003f);
    }

    @Test
    void setPan_validValue_appliesValueToLine() {
        when(line.getControl(FloatControl.Type.PAN)).thenReturn(floatControl);

        audioAdapter.setPan(line, 0.2);

        verify(floatControl).setValue(0.2f);
    }
}
