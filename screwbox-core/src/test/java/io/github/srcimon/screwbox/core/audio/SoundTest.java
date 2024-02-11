package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.utils.Resources;
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
    void fromSoundData_validWav_hasContent() {
        var content = Resources.loadBinary("kill.wav");
        var sound = Sound.fromSoundData(content);

        assertThat(sound.content()).hasSizeGreaterThan(10000);
        assertThat(sound.duration()).isEqualTo(Duration.ofMillis(186));
        assertThat(sound.sourceFormat()).isEqualTo(Sound.SourceFormat.WAV_MONO);
    }

    @Test
    void sourceFormat_sourceIsMonoWav_isMonoWav() {
        var sound = Sound.fromFile("kill.wav");

        assertThat(sound.sourceFormat()).isEqualTo(Sound.SourceFormat.WAV_MONO);
    }

    @Test
    void sourceFormat_sourceIsStereoWav_isStereoWav() {
        var sound = Sound.fromFile("stereo.wav");

        assertThat(sound.sourceFormat()).isEqualTo(Sound.SourceFormat.WAV_STEREO);
    }

    @Test
    void fromSoundData_contentNull_exception() {
        assertThatThrownBy(() -> Sound.fromSoundData(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("content must not be null");
    }

    @Test
    void fromFile_textFile_exception() {
        assertThatThrownBy(() -> Sound.fromFile("not-a-wav.txt"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("audio only supports WAV- and MIDI-Files at the moment.");
    }

    @Test
    void fromFile_existingMidi_hasContent() {
        Sound sound = Sound.fromFile("real.mid");

        assertThat(sound.content()).hasSizeGreaterThan(10);
        assertThat(sound.sourceFormat()).isEqualTo(Sound.SourceFormat.MIDI_MONO);
        assertThat(sound.duration()).isEqualTo(Duration.ofMillis(9000));
    }

    @Test
    void fromFile_invalidMidi_throwsException() {
        assertThatThrownBy(() -> Sound.fromFile("fake.mid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("could not load audio content");
    }

    @Test
    void fromFile_existingWav_hasContent() {
        Sound sound = Sound.fromFile("kill.wav");

        assertThat(sound.content()).hasSizeGreaterThan(10000);
        assertThat(sound.sourceFormat()).isEqualTo(Sound.SourceFormat.WAV_MONO);
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

        assertThat(asset.get().content()).hasSizeGreaterThan(10000);
    }
}
