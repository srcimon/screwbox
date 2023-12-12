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
public class TweenStateComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Time startTime = Time.now();
    public Percent progress = Percent.min();
    public boolean reverse;
    public Duration duration;
    public boolean isLooped;

    public TweenStateComponent(final Duration duration) {
        this(duration, false);
    }

    public TweenStateComponent(final Duration duration, final boolean isLooped) {
        this(duration, isLooped, false);
    }

    public TweenStateComponent(final Duration duration, final boolean isLooped, boolean reverse) {
        this.duration = duration;
        this.isLooped = isLooped;
        this.reverse = reverse;
    }
}
