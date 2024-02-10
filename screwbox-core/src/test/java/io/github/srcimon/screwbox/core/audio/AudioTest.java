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
        audio.playEffect(SOUND);

        verify(audio).playEffect(SOUND, SoundOptions.playOnce());
    }

    @Test
    void playEffect_soundAsset_playsSoundFromAsset() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        audio.playEffect(soundAsset);

        verify(audio).playEffect(SOUND);
    }

    @Test
    void playEffect_soundAssetWithOptions_playsSoundFromAssetWithOptions() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        audio.playEffect(soundAsset, SoundOptions.playLooped());

        verify(audio).playEffect(SOUND, SoundOptions.playLooped());
    }

    @Test
    void playMusic_sound_playsSound() {
        audio.playMusic(SOUND);

        verify(audio).playMusic(SOUND, SoundOptions.playOnce());
    }

    @Test
    void playMusic_soundAsset_playsSoundFromAsset() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        audio.playMusic(soundAsset);

        verify(audio).playMusic(SOUND);
    }

    @Test
    void playMusic_soundAssetWithOptions_playsSoundFromAssetWithOptions() {
        Asset<Sound> soundAsset = Asset.asset(() -> SOUND);

        audio.playMusic(soundAsset, SoundOptions.playLooped());

        verify(audio).playMusic(SOUND, SoundOptions.playLooped());
    }
}
