package io.github.srcimon.screwbox.core.audio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Percent.max;
import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void muteMusic_setsMusicMuted() {
        configuration.muteMusic();

        assertThat(configuration.isMusicMuted()).isTrue();

        verify(listener).configurationChanged(argThat(event -> event.changedProperty().equals(MUSIC_VOLUME)));
    }

    @Test
    void unmuteMusic_musicMuted_isUnmuted() {
        configuration.muteMusic();

        configuration.unmuteMusic();

        assertThat(configuration.isMusicMuted()).isFalse();

        verify(listener, times(2)).configurationChanged(argThat(event -> event.changedProperty().equals(MUSIC_VOLUME)));
    }

    @Test
    void setMuted_mutesMusicAndEffects() {
        configuration.setMuted(true);

        assertThat(configuration.isMusicMuted()).isTrue();
        assertThat(configuration.areEffectsMuted()).isTrue();


        verify(listener).configurationChanged(argThat(event -> event.changedProperty().equals(EFFECTS_VOLUME)));
        verify(listener).configurationChanged(argThat(event -> event.changedProperty().equals(MUSIC_VOLUME)));
    }


    @Test
    void isMuted_musicAndEffectsMuted_isTrue() {
        configuration.muteMusic();
        configuration.muteEffects();

        assertThat(configuration.isMuted()).isTrue();
    }

    @Test
    void isMuted_onlyMusicMuted_isFalse() {
        configuration.muteMusic();

        assertThat(configuration.isMuted()).isFalse();
    }

    @Test
    void isMuted_onlyEffectsMuted_isFalse() {
        configuration.muteEffects();

        assertThat(configuration.isMuted()).isFalse();
    }

    @Test
    void mute_mutesMusicAndEfects() {
        configuration.mute();

        assertThat(configuration.areEffectsMuted()).isTrue();
        assertThat(configuration.isMusicMuted()).isTrue();
    }

    @Test
    void unmute_unmutesMusicAndEfects() {
        configuration.mute();
        configuration.unmute();

        assertThat(configuration.areEffectsMuted()).isFalse();
        assertThat(configuration.isMusicMuted()).isFalse();
    }

    @Test
    void setSoundRange_rangeZero_throwsException() {
        assertThatThrownBy(() -> configuration.setSoundRange(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("sound range must be positive");
    }

    @Test
    void setSoundRange_positiveRange_setsRangeAndNotifiesListeners() {
        configuration.setSoundRange(4);

        assertThat(configuration.soundRange()).isEqualTo(4);
        verify(listener).configurationChanged(argThat(event -> event.changedProperty().equals(SOUND_RANGE)));
    }

    @Test
    void addListener_listenerNull_throwsException() {
        assertThatThrownBy(() -> configuration.addListener(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("listener must not be null");
    }
}
