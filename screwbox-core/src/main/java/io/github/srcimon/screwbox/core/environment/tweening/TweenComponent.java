package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Adds tweening to an {@link Entity}.
 */
public class TweenComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Time startTime = Time.now();
    public Percent progress = Percent.min();
    public boolean reverse = false;
    public Duration duration;
    public boolean isLooped;

    public TweenComponent(final Duration duration) {
        this(duration, false);
    }

    public TweenComponent(final Duration duration, final boolean isLooped) {
        this.duration = duration;
        this.isLooped = isLooped;
    }
}