package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;

import java.util.Optional;

/**
 * Information about currently playing {@link Sound}. Retrieved via {@link Audio#activePlaybacks()}.
 */
public class Playback {

    private final Sound sound;
    private final SoundOptions options;
    private final Vector position;
    private final Time startTime = Time.now();

    /**
     * Constructor used by the engine.
     */
    public Playback(final Sound sound, final SoundOptions options, final Vector position) {
        this.sound = sound;
        this.options = options;
        this.position = position;
    }

    /**
     * The current progress at a specified {@link Time} of the {@link Playback} using {@link #startTime()} and {@link Sound#duration()}.
     *
     * @see #progress()
     */
    public Percent progress(final Time time) {
        return sound.duration().progress(startTime, time);
    }

    /**
     * The current progress of the {@link Playback} using {@link #startTime()} and {@link Sound#duration()}.
     *
     * @see #progress(Time)
     */
    public Percent progress() {
        return progress(Time.now());
    }

    /**
     * The time the {@link Playback} started.
     */
    public Time startTime() {
        return startTime;
    }

    /**
     * The {@link Sound} that is played.
     */
    public Sound sound() {
        return sound;
    }

    /**
     * The {@link SoundOptions} that were used to play the {@link Sound}.
     */
    public SoundOptions options() {
        return options;
    }

    /**
     * The position of the {@link Sound} when a {@link Sound} was played via {@link Audio#playSound(Sound, Vector)}.
     */
    public Optional<Vector> position() {
        return Optional.ofNullable(position);
    }

}
