package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.graphics.Camera;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DynamicSoundSupportTest {

    @Mock
    Camera camera;

    @Mock
    AudioConfiguration configuration;

    @InjectMocks
    DynamicSoundSupport dynamicSoundSupport;

    @Test
    void currentVolume_nothingConfigured_isMax() {
        when(configuration.effectVolume()).thenReturn(Percent.max());

        var volume = dynamicSoundSupport.currentVolume(SoundOptions.playOnce());

        assertThat(volume).isEqualTo(Percent.max());
    }

    @Test
    void currentVolume_halfVolumeAndOptionSet_isMixOfSettings() {
        when(configuration.musicVolume()).thenReturn(Percent.half());

        var volume = dynamicSoundSupport.currentVolume(SoundOptions.playOnce().volume(Percent.of(0.3)).asMusic());

        assertThat(volume).isEqualTo(Percent.of(0.15));
    }

    @Test
    void currentVolume_asMusicAndMusicIsMuted_isZero() {
        when(configuration.isMusicMuted()).thenReturn(true);

        var volume = dynamicSoundSupport.currentVolume(SoundOptions.playOnce().asMusic());

        assertThat(volume).isEqualTo(Percent.zero());
    }

    @Test
    void currentPan_noPositionAndNoSetting_isZero() {
        var pan = dynamicSoundSupport.currentPan(SoundOptions.playTimes(3));

        assertThat(pan).isZero();
    }

    @Test
    void currentVolume_outOfSoundRange_isZero() {
        when(configuration.effectVolume()).thenReturn(Percent.max());
        when(configuration.soundRange()).thenReturn(200.0);
        when(camera.position()).thenReturn(Vector.zero());

        var volume = dynamicSoundSupport.currentVolume(SoundOptions.playTimes(3).position(Vector.$(0,1000)));

        assertThat(volume).isEqualTo(Percent.zero());
    }
}
