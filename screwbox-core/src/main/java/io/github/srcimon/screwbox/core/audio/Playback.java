package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;

import java.util.Optional;

//TODO Test and javadoc
public class Playback {

    private final Sound sound;
    private final SoundOptions options;
    private final Vector position;
    private final Time startTime = Time.now();

    public Playback(final Sound sound, final SoundOptions options, final Vector position) {
        this.sound = sound;
        this.options = options;
        this.position = position;
    }

    public Percent progress() {
        return Percent.of(1.0 * Duration.since(startTime).nanos() / sound.duration().nanos());
    }

    public Time startTime() {
        return startTime;
    }

    public Sound sound() {
        return sound;
    }

    public SoundOptions options() {
        return options;
    }

    public Optional<Vector> position() {
        return Optional.ofNullable(position);
    }
}
