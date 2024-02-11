package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoundOptionsTest {

    @Test
    void soundOptions_loopedWithHalfVolume_hasInfinitePlaybacksAtHalfVolume() {
        var options = SoundOptions.playLooped().volume(Percent.half());

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
    void soundOptions_panAndBalanceOutOfLowerBounds_hasValidPanAndBalance() {
        var options = SoundOptions.playOnce().pan(-4).balance(-9);

        assertThat(options.pan()).isEqualTo(-1);
        assertThat(options.balance()).isEqualTo(-1);
    }

    @Test
    void soundOptions_panAndBalanceOutOfUpperBounds_hasValidPanAndBalance() {
        var options = SoundOptions.playOnce().pan(4).balance(9);

        assertThat(options.pan()).isEqualTo(1);
        assertThat(options.balance()).isEqualTo(1);
    }

    @Test
    void soundOptions_validInputForPanAndBalance_hasPanAndBalance() {
        var options = SoundOptions.playOnce().pan(0.2).balance(-0.3);

        assertThat(options.pan()).isEqualTo(0.2);
        assertThat(options.balance()).isEqualTo(-0.3);
    }

    @Test
    void playTimes_timesIsZero_throwsException() {
        assertThatThrownBy(() -> SoundOptions.playTimes(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("sound must be played at least once");
    }
}
