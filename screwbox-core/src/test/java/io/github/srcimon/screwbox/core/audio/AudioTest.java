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

    @Spy
    Audio audio;

    @Test
    void playEffect_soundAsset_playsSoundFromAsset() {
        Sound sound = Sound.fromFile("kill.wav");
        Asset<Sound> soundAsset = Asset.asset(() -> sound);

        audio.playEffect(soundAsset);

        verify(audio).playEffect(sound);
    }

    @Test
    void muteEffects_callsSetEffectsMuted() {
        audio.muteEffects();

        verify(audio).setEffectsMuted(true);
    }

    @Test
    void unmuteEffects_callsSetEffectsMuted() {
        audio.unmuteEffects();

        verify(audio).setEffectsMuted(false);
    }

    @Test
    void muteMusic_callsSetMusicMuted() {
        audio.muteMusic();

        verify(audio).setMusicMuted(true);
    }

    @Test
    void unmuteMusic_callsSetMusicMuted() {
        audio.unmuteMusic();

        verify(audio).setMusicMuted(false);
    }

    @Test
    void setMuted_mutesMusicAndEffects() {
        audio.setMuted(true);

        verify(audio).setMusicMuted(true);
        verify(audio).setEffectsMuted(true);
    }

    @Test
    void isMuted_musicAndEffectsMuted_isTrue() {
        when(audio.isMusicMuted()).thenReturn(true);
        when(audio.areEffectsMuted()).thenReturn(true);

        assertThat(audio.isMuted()).isTrue();
    }

    @Test
    void isMuted_onlyMusicMuted_isFalse() {
        when(audio.isMusicMuted()).thenReturn(true);
        when(audio.areEffectsMuted()).thenReturn(false);

        assertThat(audio.isMuted()).isFalse();
    }

    @Test
    void isMuted_onlyEffectsMuted_isFalse() {
        when(audio.isMusicMuted()).thenReturn(false);

        assertThat(audio.isMuted()).isFalse();
    }

    @Test
    void mute_mutesMusicAndEfects() {
        audio.mute();

        verify(audio).setMusicMuted(true);
        verify(audio).setEffectsMuted(true);
    }

    @Test
    void unmute_unmutesMusicAndEfects() {
        audio.unmute();

        verify(audio).setMusicMuted(false);
        verify(audio).setEffectsMuted(false);
    }
}
