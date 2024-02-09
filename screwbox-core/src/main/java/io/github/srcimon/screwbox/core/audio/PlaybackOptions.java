package io.github.srcimon.screwbox.core.audio;

public class PlaybackOptions {

    public static PlaybackOptions playLooped() {
        return new PlaybackOptions(Integer.MAX_VALUE);
    }

    public static PlaybackOptions playOnce() {
        return playTimes(1);
    }

    public static PlaybackOptions playTimes(final int times) {
        return new PlaybackOptions(times);
    }

    private PlaybackOptions(final int playbackRepetitions) {
        this.playbackRepetitions = playbackRepetitions;
    }

    private final int playbackRepetitions;
}
