package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
}
