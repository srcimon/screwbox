package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Percent.max;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AudioConfigurationTest {

    AudioConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new AudioConfiguration();
    }


    @Test
    void muteMusic_musicUnmuted_mutesMusic() {
        configuration.muteMusic();

        assertThat(configuration.musicVolume()).isEqualTo(max());
        assertThat(configuration.effectVolume()).isEqualTo(max());
        assertThat(configuration.isMusicMuted()).isTrue();
        assertThat(configuration.areEffectsMuted()).isFalse();
    }

    @Test
    void muteEffects_effectUnmuted_mutesEffects() {
        configuration.muteEffects();

        assertThat(configuration.musicVolume()).isEqualTo(max());
        assertThat(configuration.effectVolume()).isEqualTo(max());
        assertThat(configuration.isMusicMuted()).isFalse();
        assertThat(configuration.areEffectsMuted()).isTrue();
    }

    /*
    @Test
    void mute_allUnmuted_mutesEffectsAndMusic() {
        audio.configuration().mute();

        assertThat(audio.configuration().musicVolume()).isEqualTo(max());
        assertThat(audio.configuration().effectVolume()).isEqualTo(max());
        assertThat(audio.configuration().isMusicMuted()).isTrue();
        assertThat(audio.configuration().areEffectsMuted()).isTrue();
    }

    @Test
    void unmuteEffects_effectsHadVolumeConfigBefore_returnsOldVolume() {
        when(audioAdapter.createClip(any())).thenReturn(clip);
        audio.configuration().setEffectVolume(Percent.of(0.7));
        audio.configuration().muteEffects();

        audio.configuration().unmute();

        Sound sound = Sound.dummyEffect();
        audio.playEffect(sound);

        awaitShutdown();

        verify(audioAdapter).createClip(sound);
        verify(audioAdapter).setVolume(clip, Percent.of(0.7));
    }

    @Test
    void muteEffects_callsSetEffectsMuted() {
        configuration.muteEffects();

        verify(configuration).setEffectsMuted(true);
    }

    @Test
    void unmuteEffects_callsSetEffectsMuted() {
        configuration.unmuteEffects();

        verify(configuration).setEffectsMuted(false);
    }

    @Test
    void muteMusic_callsSetMusicMuted() {
        configuration.muteMusic();

        verify(configuration).setMusicMuted(true);
    }

    @Test
    void unmuteMusic_callsSetMusicMuted() {
        configuration.unmuteMusic();

        verify(configuration).setMusicMuted(false);
    }

    @Test
    void setMuted_mutesMusicAndEffects() {
        configuration.setMuted(true);

        verify(configuration).setMusicMuted(true);
        verify(configuration).setEffectsMuted(true);
    }

    @Test
    void isMuted_musicAndEffectsMuted_isTrue() {
        when(configuration.isMusicMuted()).thenReturn(true);
        when(configuration.areEffectsMuted()).thenReturn(true);

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
    */

}
