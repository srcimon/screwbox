package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;

/**
 * Sets options for the playback of a specific {@link Sound} via {@link Audio}.
 */
public record SoundOptions(int times, Percent volume, double balance, double pan, boolean isMusic) {

    /**
     * Playback {@link Sound} until stopped via {@link Audio#stopSound(Sound)}.
     */
    public static SoundOptions playLooped() {
        return playTimes(Integer.MAX_VALUE);
    }

    /**
     * Playback {@link Sound} only once.
     */
    public static SoundOptions playOnce() {
        return playTimes(1);
    }

    /**
     * Playback {@link Sound} looped for the given times.
     */
    public static SoundOptions playTimes(final int times) {
        if (times < 1) {
            throw new IllegalArgumentException("sound must be played at least once");
        }
        return new SoundOptions(times, Percent.max(), 0, 0, false);
    }

    /**
     * {@link Sound} should be played as music using {@link AudioConfiguration#musicVolume()}.
     */
    public SoundOptions asMusic() {
        return new SoundOptions(times, volume, balance, pan, true);
    }

    /**
     * Sets the volume of the playback. The actual playback volume is also determined by {@link AudioConfiguration}.
     */
    public SoundOptions volume(final Percent volume) {
        return new SoundOptions(times, volume, balance, pan, isMusic);
    }

    /**
     * Sets the pan of the playback. Allowed range is -1 to 1. Auto crops values out of this range.
     */
    public SoundOptions pan(final double pan) {
        return new SoundOptions(times, volume, balance, Math.clamp(pan, -1, 1), isMusic);
    }

    /**
     * Sets the balance of the playback. Allowed range is -1 to 1. Auto crops values out of this range.
     */
    public SoundOptions balance(final double balance) {
        return new SoundOptions(times, volume, Math.clamp(balance, -1, 1), pan, isMusic);
    }

    /**
     * {@link Sound} should be played as effect using {@link AudioConfiguration#effectVolume()}.
     */
    public boolean isEffect() {
        return !isMusic;
    }
}
