package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;

import java.util.Objects;

import static io.github.srcimon.screwbox.core.utils.MathUtil.clamp;

/**
 * Sets options for the playback of a specific {@link Sound} via {@link Audio#playSound(Sound, SoundOptions)} or {@link Audio#playMusic(Sound, SoundOptions)}.
 */
public class SoundOptions {

    private final int times;
    private Percent volume = Percent.max();
    private double balance = 0;
    private double pan = 0;
    boolean isMusic = false;

    /**
     * Playback {@link Sound} until stopped via {@link Audio#stopSound(Sound)}.
     */
    public static SoundOptions playLooped() {
        return new SoundOptions(Integer.MAX_VALUE);
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
        return new SoundOptions(times);
    }

    //TODO Test and javadoc
    public boolean isMusic() {
        return isMusic;
    }

    //TODO Test and javadoc
    public boolean isEffect() {
        return !isMusic;
    }

    //TODO Test and javadoc
    public SoundOptions asMusic() {
        isMusic = true;
        return this;
    }

    private SoundOptions(final int times) {
        this.times = times;
    }

    /**
     * Sets the volume of the playback. The actual playback volume is also determined by {@link AudioConfiguration}.
     */
    public SoundOptions volume(final Percent volume) {
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
     * Sets the pan of the playback. Allowed range is -1 to 1. Auto crops values out of this range.
     */
    public SoundOptions pan(final double pan) {
        this.pan = clamp(-1, pan, 1);
        return this;
    }

    /**
     * Get the pan.
     */
    public double pan() {
        return pan;
    }

    /**
     * Sets the balance of the playback. Allowed range is -1 to 1. Auto crops values out of this range.
     */
    public SoundOptions balance(final double balance) {
        this.balance = clamp(-1, balance, 1);
        return this;
    }

    /**
     * Get the balance.
     */
    public double balance() {
        return balance;
    }

    /**
     * Get the number of time the sound is played.
     */
    public int times() {
        return times;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundOptions that = (SoundOptions) o;

        if (times != that.times) return false;
        return Objects.equals(volume, that.volume);
    }

    @Override
    public int hashCode() {
        int result = times;
        result = 31 * result + (volume != null ? volume.hashCode() : 0);
        return result;
    }
}
