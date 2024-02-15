package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.assets.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AudioTest {

    public static final Sound SOUND = Sound.fromFile("kill.wav");

    @Spy
    Audio audio;

    @Test
    void playSound_sound_playsSound() {
        audio.playSound(SOUND);

        verify(audio).playSound(SOUND, SoundOptions.playOnce());
    }

    @Test
    void playSound_soundAsset_playsSoundFromAsset() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        audio.playSound(soundAsset);

        verify(audio).playSound(SOUND);
    }

    @Test
    void playSound_soundAssetWithOptions_playsSoundFromAssetWithOptions() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        audio.playSound(soundAsset, SoundOptions.playLooped());

        verify(audio).playSound(SOUND, SoundOptions.playLooped());
    }

    @Test
    void stopSound_assetGiven_stopsSoundFromAsset() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        audio.stopSound(soundAsset);

        verify(audio).stopSound(SOUND);
    }

    @Test
    void playSound_soundAssetAndPosition_playsSoundAtPosition() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        audio.playSound(soundAsset, $(10, 20));

        verify(audio).playSound(SOUND, $(10, 20));
    }

    @Test
    void isActive_supplierGiven_returnsIsActiveOfSound() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        when(audio.isActive(SOUND)).thenReturn(true);

        assertThat(audio.isActive(soundAsset)).isTrue();
    }

    @Test
    void activeCount_supplierGiven_returnsActiveCountSound() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        when(audio.activeCount(SOUND)).thenReturn(6);

        assertThat(audio.activeCount(soundAsset)).isEqualTo(6);
    }
}
