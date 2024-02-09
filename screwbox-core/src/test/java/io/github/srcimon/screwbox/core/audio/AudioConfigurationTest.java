package io.github.srcimon.screwbox.core.audio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Percent.max;
import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.EFFECTS_VOLUME;
import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.MUSIC_VOLUME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AudioConfigurationTest {

    AudioConfiguration configuration;

    @Mock
    AudioConfigurationListener listener;

    @BeforeEach
    void setUp() {
        configuration = new AudioConfiguration();
        configuration.addListener(listener);
    }


    @Test
    void muteMusic_musicUnmuted_mutesMusic() {
        configuration.muteMusic();

        assertThat(configuration.musicVolume()).isEqualTo(max());
        assertThat(configuration.effectVolume()).isEqualTo(max());
        assertThat(configuration.isMusicMuted()).isTrue();
        assertThat(configuration.areEffectsMuted()).isFalse();
        verify(listener).configurationChanged(argThat(event -> event.changedProperty().equals(MUSIC_VOLUME)));
    }

    @Test
    void muteEffects_effectUnmuted_mutesEffects() {
        configuration.muteEffects();

        assertThat(configuration.musicVolume()).isEqualTo(max());
        assertThat(configuration.effectVolume()).isEqualTo(max());
        assertThat(configuration.isMusicMuted()).isFalse();
        assertThat(configuration.areEffectsMuted()).isTrue();
        verify(listener).configurationChanged(argThat(event -> event.changedProperty().equals(EFFECTS_VOLUME)));
    }

    @Test
    void mute_allUnmuted_mutesEffectsAndMusic() {
        configuration.mute();

        assertThat(configuration.musicVolume()).isEqualTo(max());
        assertThat(configuration.effectVolume()).isEqualTo(max());
        assertThat(configuration.isMusicMuted()).isTrue();
        assertThat(configuration.areEffectsMuted()).isTrue();
        verify(listener).configurationChanged(argThat(event -> event.changedProperty().equals(EFFECTS_VOLUME)));
        verify(listener).configurationChanged(argThat(event -> event.changedProperty().equals(MUSIC_VOLUME)));
    }

    @Test
    void unmuteEffects_effectsMuted_setsEffectsUnmuted() {
        configuration.muteEffects();

        configuration.unmuteEffects();

        assertThat(configuration.areEffectsMuted()).isFalse();
        verify(listener, times(2)).configurationChanged(argThat(event -> event.changedProperty().equals(EFFECTS_VOLUME)));
    }

    @Test
    void muteEffects_setEffectsMuted() {
        configuration.muteEffects();

        assertThat(configuration.areEffectsMuted()).isTrue();
        verify(listener).configurationChanged(argThat(event -> event.changedProperty().equals(EFFECTS_VOLUME)));
    }
    
    /*
    

   


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
