package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.assets.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AudioTest {

    public static final Sound SOUND = Sound.fromFile("kill.wav");
    public static final Asset<Sound> ASSET = Asset.asset(() -> SOUND);

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
    void stopSound_assetGiven_stopsSoundFromAsset() {
        audio.stopSound(ASSET);

        verify(audio).stopSound(SOUND);
    }


    @Test
    void isActive_supplierGiven_returnsStatusOfSound() {
        when(audio.isActive(SOUND)).thenReturn(true);

        assertThat(audio.isActive(ASSET)).isTrue();
    }

    @Test
    void activeCount_supplierGiven_returnsActiveCountSound() {
        when(audio.activeCount(SOUND)).thenReturn(6);

        assertThat(audio.activeCount(ASSET)).isEqualTo(6);
    }
}
