package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.SoundBundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlaybackTest {

    @Test
    void progress_afterAShortTime_isNotZeroAndNotMax() {
        Time before = Time.now();
        var playback = new Playback(SoundBundle.PHASER.get(), SoundOptions.playOnce().asMusic(), null);
        Time after = Time.now();

        assertThat(playback.progress()).isNotEqualTo(Percent.zero()).isNotEqualTo(Percent.max());
        assertThat(playback.startTime().isAfter(before)).isTrue();
        assertThat(playback.startTime().isBefore(after)).isTrue();
    }
}