package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.graphics.internal.AttentionFocus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
class DynamicSoundSupportTest {

    @Mock
    AttentionFocus attentionFocus;

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
    void currentVolume_effectAndEffectIsMuted_isZero() {
        when(configuration.areEffectsMuted()).thenReturn(true);

        var volume = dynamicSoundSupport.currentVolume(SoundOptions.playOnce());

        assertThat(volume).isEqualTo(Percent.zero());
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
    void currentPan_positionSetLeftOfCamera_panIsRight() {
        when(attentionFocus.distanceTo($(-40, 0))).thenReturn(40.0);
        when(attentionFocus.direction($(-40, 0))).thenReturn(Vector.x(-1));
        when(configuration.soundRange()).thenReturn(200.0);

        var pan = dynamicSoundSupport.currentPan(SoundOptions.playOnce().position($(-40, 0)));

        assertThat(pan).isEqualTo(-0.2);
    }

    @Test
    void currentPan_positionSetRightOfCamera_panIsLeft() {
        when(attentionFocus.distanceTo($(80, 0))).thenReturn(80.0);
        when(attentionFocus.direction($(80, 0))).thenReturn(Vector.x(1.0));
        when(configuration.soundRange()).thenReturn(200.0);

        var pan = dynamicSoundSupport.currentPan(SoundOptions.playOnce().position($(80, 0)));

        assertThat(pan).isEqualTo(0.4);
    }

    @Test
    void currentVolume_outOfSoundRange_isZero() {
        when(configuration.effectVolume()).thenReturn(Percent.max());
        when(configuration.soundRange()).thenReturn(200.0);
        when(attentionFocus.distanceTo($(0, 1000.0))).thenReturn(1000.0);

        var volume = dynamicSoundSupport.currentVolume(SoundOptions.playTimes(3).position($(0, 1000)));

        assertThat(volume).isEqualTo(Percent.zero());
    }
}
