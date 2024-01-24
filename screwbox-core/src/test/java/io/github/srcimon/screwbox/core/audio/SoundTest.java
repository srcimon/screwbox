package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.utils.Resources;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoundTest {

    @Test
    void dummyEffect_returnsDummySoundEffect() {
        var sound = Sound.dummyEffect();

        assertThat(sound.content()).hasSizeGreaterThan(10000);
        assertThat(sound.duration()).isEqualTo(Duration.ofMillis(1033));
    }
    
    @Test
    void fromWav_validWav_hasContent() {
        var content = Resources.loadBinary("kill.wav");
        var sound = Sound.fromWav(content);

        assertThat(sound.content()).hasSizeGreaterThan(10000);
        assertThat(sound.duration()).isEqualTo(Duration.ofMillis(186));
    }

    @Test
    void fromWav_contentNull_exception() {
        assertThatThrownBy(() -> Sound.fromWav(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("content must not be null");
    }

    @Test
    void fromFile_textFile_exception() {
        assertThatThrownBy(() -> Sound.fromFile("not-a-wav.txt"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Audio only supports WAV- and MIDI-Files at the moment.");
    }

    //TODO FIX WITH REAL MIDI
    @Test
    @Disabled
    void fromFile_existingMidi_hasContent() {
        Sound sound = Sound.fromFile("fake.mid");

        assertThat(sound.content()).hasSizeGreaterThan(10);
        assertThat(sound.format()).isEqualTo(Sound.Format.MIDI);
    }

    @Test
    void fromFile_existingWav_hasContent() {
        Sound sound = Sound.fromFile("kill.wav");

        assertThat(sound.content()).hasSizeGreaterThan(10000);
        assertThat(sound.format()).isEqualTo(Sound.Format.WAV);
    }

    @Test
    void fromFile_fileNameNull_exception() {
        assertThatThrownBy(() -> Sound.fromFile(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void assetFromFile_createsAsset() {
        Asset<Sound> asset = Sound.assetFromFile("kill.wav");

        Sound sound = asset.get();
        assertThat(sound.content()).hasSizeGreaterThan(10000);
    }
}
