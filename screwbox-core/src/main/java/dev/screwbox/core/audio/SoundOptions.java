package dev.screwbox.core.audio;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

/**
 * Sets options for the playback of a specific {@link Sound} via {@link Audio}.
 *
 * @param times      number of times the {@link Sound} will be played.
 * @param volume     volume of the {@link Playback}
 * @param pan        pan of the playback. Allowed range is -1 to 1.
 * @param isMusic    mark the {@link Sound} as music
 * @param position   dynamically set {@link #volume()} and {@link #pan()} by specified position
 * @param speed      speed of the playback (0.1 to 10.0), also affects pitch
 * @param randomness add randomness to the {@link #speed()} of the playback
 */
public record SoundOptions(int times, Percent volume, double pan, boolean isMusic,
                           Vector position, double speed, double randomness) implements Serializable {

    private static final Random RANDOM = new Random();

    @Serial
    private static final long serialVersionUID = 1L;

    public SoundOptions {
        Validate.range(speed, 0.1, 10, "speed is out of valid range (0.1 to 10.0): " + speed);
        Validate.range(pan, -1, 1, "pan is out of valid range (-1 to 1): " + pan);
        Validate.range(randomness, 0, 10, "randomness must be in valid rang (0 to 10.0)");
    }

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
        return new SoundOptions(times, Percent.max(), 0, false, null, 1, 0);
    }

    /**
     * {@link Sound} should be played as music using {@link AudioConfiguration#musicVolume()}.
     */
    public SoundOptions asMusic() {
        return new SoundOptions(times, volume, pan, true, null, speed, randomness);
    }

    /**
     * Sets a position as source of the {@link Sound}. {@link #pan()} and {@link #volume()} will be dynamically changed
     * based upon the distance between the specified position and the {@link Camera#position()}.
     */
    public SoundOptions position(final Vector position) {
        return new SoundOptions(times, volume, pan, isMusic, position, speed, randomness);
    }

    /**
     * Sets the volume of the playback. The actual playback volume is also determined by {@link AudioConfiguration}.
     */
    public SoundOptions volume(final Percent volume) {
        return new SoundOptions(times, volume, pan, isMusic, position, speed, randomness);
    }

    /**
     * Sets the pan of the playback. Allowed range is -1 to 1.
     */
    public SoundOptions pan(final double pan) {
        return new SoundOptions(times, volume, pan, isMusic, position, speed, randomness);
    }

    /**
     * Sets the speed of the playback. Allowed range is 0.1 to 10. Speed cannot be changed once a {@link Sound}
     * is started. Will only accept values in 0.1 steps. (0.1, 0.2...). Will automatically fix values accordingly.
     */
    public SoundOptions speed(final double speed) {
        return new SoundOptions(times, volume, pan, isMusic, position, ensureValidSpeedValue(speed), randomness);
    }

    /**
     * The actual speed that should be used for the next playback of the {@link Sound}. Will consider {@link #speed()}
     * and {@link #randomness()}.
     *
     * @since 3.4.0
     */
    public double playbackSpeed() {
        return randomness == 0
                ? speed
                : Math.clamp(ensureValidSpeedValue(speed + RANDOM.nextDouble(-randomness, randomness)), 0.1, 10.0);
    }

    public SoundOptions randomness(final double randomness) {
        return new SoundOptions(times, volume, pan, isMusic, position, speed, ensureValidSpeedValue(randomness));
    }

    /**
     * {@link Sound} should be played as effect using {@link AudioConfiguration#effectVolume()}.
     */
    public boolean isEffect() {
        return !isMusic;
    }

    private double ensureValidSpeedValue(double speed) {
        return Math.round(speed * 10.0) / 10.0;
    }

}