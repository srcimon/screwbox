package de.suzufa.screwbox.core.audio.internal;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sound.sampled.FloatControl;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.audio.Sound;

class AudioAdapterTest {

    @Test
    void createClip_fullVolume_returnsClipWithCorrectVolume() {
        Sound sound = Sound.fromFile("kill.wav");
        var clip = new AudioAdapter().createClip(sound, Percent.max());
        var gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        assertThat(gainControl.getValue()).isZero();
    }
}
