package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;

/**
 * Sets options for the playback of a specific {@link Sound} via {@link Audio}.
 */
public record SoundOptions(int times, Percent volume, double pan, boolean isMusic,
                           Vector position) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Playback {@link Sound} until stopped via {@link Audio#stopAllPlaybacks(Sound)}.
     */
    public static SoundOptions playContinuously() {
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
        Validate.positive(times, "sound must be played at least once");
        return new SoundOptions(times, Percent.max(), 0, false, null);
    }

    /**
     * {@link Sound} should be played as music using {@link AudioConfiguration#musicVolume()}.
     */
    public SoundOptions asMusic() {
        return new SoundOptions(times, volume, pan, true, null);
    }

    /**
     * Sets a postion as source of the {@link Sound}. {@link #pan()} and {@link #volume()} will be dynamicly changed
     * based upon the distance between the specified postion and the {@link Camera#position()}.
     */
    public SoundOptions position(Vector position) {
        return new SoundOptions(times, volume, pan, isMusic, position);
    }

    /**
     * Sets the volume of the playback. The actual playback volume is also determined by {@link AudioConfiguration}.
     */
    public SoundOptions volume(final Percent volume) {
        return new SoundOptions(times, volume, pan, isMusic, position);
    }

    /**
     * Sets the pan of the playback. Allowed range is -1 to 1. Auto crops values out of this range.
     */
    public SoundOptions pan(final double pan) {
        return new SoundOptions(times, volume, Math.clamp(pan, -1, 1), isMusic, position);
    }

    /**
     * {@link Sound} should be played as effect using {@link AudioConfiguration#effectVolume()}.
     */
    public boolean isEffect() {
        return !isMusic;
    }
}
