package de.suzufa.screwbox.core.audio;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.assets.Asset;

@ExtendWith(MockitoExtension.class)
class AudioTest {

    @Spy
    Audio audio;

    @Test
    void playEffect_soundAsset_playsSoundFromAsset() {
        Sound sound = Sound.fromFile("kill.wav");
        Asset<Sound> soundAsset = Asset.asset(() -> sound);

        audio.playEffect(soundAsset);

        verify(audio).playEffect(sound);
    }
}
