package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.assets.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AudioTest {

    public static final Sound SOUND = Sound.fromFile("kill.wav");

    @Spy
    Audio audio;

    @Test
    void playEffect_sound_playsSound() {
        audio.playSound(SOUND);

        verify(audio).playSound(SOUND, SoundOptions.playOnce());
    }

    @Test
    void playEffect_soundAsset_playsSoundFromAsset() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        audio.playSound(soundAsset);

        verify(audio).playSound(SOUND);
    }

    @Test
    void playEffect_soundAssetWithOptions_playsSoundFromAssetWithOptions() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        audio.playSound(soundAsset, SoundOptions.playLooped());

        verify(audio).playSound(SOUND, SoundOptions.playLooped());
    }
}
