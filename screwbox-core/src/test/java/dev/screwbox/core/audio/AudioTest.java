package dev.screwbox.core.audio;

import dev.screwbox.core.assets.Asset;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class AudioTest {

    private static final Sound SOUND = Sound.fromFile("kill.wav");
    private static final Asset<Sound> ASSET = Asset.asset(() -> SOUND);

    @Spy
    Audio audio;

    @Test
    void playSound_sound_playsSound() {
        audio.playSound(SOUND);

        verify(audio).playSound(SOUND, SoundOptions.playOnce());
    }

    @Test
    void playSound_soundAsset_playsSoundFromAsset() {
        audio.playSound(ASSET);

        verify(audio).playSound(SOUND);
    }

    @Test
    void playSound_soundAssetWithOptions_playsSoundFromAssetWithOptions() {
        audio.playSound(ASSET, SoundOptions.playContinuously());

        verify(audio).playSound(SOUND, SoundOptions.playContinuously());
    }

    @Test
    void stopSound_assetGiven_stopsAllPlaybacksFromAsset() {
        audio.stopAllPlaybacks(ASSET);

        verify(audio).stopAllPlaybacks(SOUND);
    }

    @Test
    void hasActivePlaybacks_supplierGiven_returnsStatusOfSound() {
        when(audio.hasActivePlaybacks(SOUND)).thenReturn(true);

        assertThat(audio.hasActivePlaybacks(ASSET)).isTrue();
    }

    @Test
    void activePlaybackCount_supplierGiven_returnsActivePlaybackCountSound() {
        when(audio.activePlaybackCount(SOUND)).thenReturn(6);

        assertThat(audio.activePlaybackCount(ASSET)).isEqualTo(6);
    }
}
