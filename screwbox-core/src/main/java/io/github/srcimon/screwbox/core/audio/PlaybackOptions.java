package io.github.srcimon.screwbox.core.audio;

//TODO TEST JAVADOC AND RENAME
public class PlaybackOptions {

    public static PlaybackOptions playLooped() {
        return new PlaybackOptions(Integer.MAX_VALUE);
    }

    public static PlaybackOptions playOnce() {
        return playTimes(1);
    }

    public static PlaybackOptions playTimes(final int times) {
        return new PlaybackOptions(times - 1);
    }

    private PlaybackOptions(final int playbackRepetitions) {
        this.playbackRepetitions = playbackRepetitions;
    }

    private final int playbackRepetitions;

    //TOODO: RENAME
    public int playbackRepetitions() {
        return playbackRepetitions;
    }
}
