package io.github.simonbas.screwbox.core.audio;

import io.github.simonbas.screwbox.core.assets.Asset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoundTest {

    @Test
    void fromFile_noWav_exception() {
        assertThatThrownBy(() -> Sound.fromFile("not-a-wav.txt"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Audio only supports WAV-Files at the moment.");
    }

    @Test
    void fromFile_existingWav_hasContent() {
        Sound sound = Sound.fromFile("kill.wav");

        assertThat(sound.content()).hasSizeGreaterThan(10000);
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
