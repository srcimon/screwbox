package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;

//TODO Test

/**
 * Sets options for the playback of a specific {@link Sound} via {@link Audio#playEffect(Sound, PlaybackOptions)} or {@link Audio#playMusic(Sound, PlaybackOptions)}.
 */
public class PlaybackOptions {

    private final int times;
    private Percent volume = Percent.max();

    /**
     * Playback {@link Sound} until stopped via {@link Audio#stop(Sound)}.
     */
    public static PlaybackOptions playLooped() {
        return new PlaybackOptions(Integer.MAX_VALUE);
    }

    /**
     * Playback {@link Sound} only once.
     */
    public static PlaybackOptions playOnce() {
        return playTimes(1);
    }

    /**
     * Playback {@link Sound} looped for the given times.
     */
    public static PlaybackOptions playTimes(final int times) {
        //TODO Validate
        return new PlaybackOptions(times);
    }

    private PlaybackOptions(final int times) {
        this.times = times;
    }

    /**
     * Sets the volume of the playback. The actual playback volume is also determined by {@link AudioConfiguration}.
     */
    public PlaybackOptions volume(final Percent volume) {
        this.volume = volume;
        return this;
    }

    /**
     * Get the playback volume.
     */
    public Percent volume() {
        return volume;
    }

    /**
     * Get the number of time the sound is played.
     */
    public int times() {
        return times;
    }


}
