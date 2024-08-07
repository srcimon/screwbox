package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoundOptionsTest {

    @Test
    void soundOptions_loopedWithHalfVolume_hasInfinitePlaybacksAtHalfVolume() {
        var options = SoundOptions.playContinuously().volume(Percent.half());

        assertThat(options.times()).isEqualTo(Integer.MAX_VALUE);
        assertThat(options.volume()).isEqualTo(Percent.half());
    }

    @Test
    void soundOptions_onePlaybackWithQuaterVolume_hasOnePlaybackAtQuaterVolume() {
        var options = SoundOptions.playOnce().volume(Percent.quater());

        assertThat(options.times()).isEqualTo(1);
        assertThat(options.volume()).isEqualTo(Percent.quater());
    }

    @Test
    void soundOptions_threePlaybacksWithoutVolumeChanged_hasThreePlaybacksAtMaxVolume() {
        var options = SoundOptions.playTimes(3);

        assertThat(options.times()).isEqualTo(3);
        assertThat(options.volume()).isEqualTo(Percent.max());
    }

    @Test
    void soundOptions_positionSet_hasPosition() {
        var options = SoundOptions.playTimes(3).position($(20,25));

        assertThat(options.position()).isEqualTo($(20,25));
    }

    @Test
    void soundOptions_panOutOfLowerBounds_hasValidPan() {
        var options = SoundOptions.playOnce().pan(-4);

        assertThat(options.pan()).isEqualTo(-1);
    }

    @Test
    void soundOptions_panOutOfUpperBounds_hasValidPan() {
        var options = SoundOptions.playOnce().pan(4);

        assertThat(options.pan()).isEqualTo(1);
    }

    @Test
    void soundOptions_validInputForPan_hasPan() {
        var options = SoundOptions.playOnce().pan(0.2);

        assertThat(options.pan()).isEqualTo(0.2);
    }

    @Test
    void soundOptions_notMarkedAsMusic_isEffect() {
        var options = SoundOptions.playOnce();

        assertThat(options.isEffect()).isTrue();
        assertThat(options.isMusic()).isFalse();
    }

    @Test
    void soundOptions_markedAsMusic_isEffect() {
        var options = SoundOptions.playOnce().asMusic();

        assertThat(options.isEffect()).isFalse();
        assertThat(options.isMusic()).isTrue();
    }

    @Test
    void playTimes_timesIsZero_throwsException() {
        assertThatThrownBy(() -> SoundOptions.playTimes(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("sound must be played at least once");
    }
}
