package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.Ease;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Adds tweening to an {@link Entity}.
 */
public class TweenComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;
    public Time startTime = Time.now();
    public Percent progress = Percent.zero();
    public Percent value = Percent.zero();
    public boolean reverse = false;

    public Duration duration;
    public boolean isLooped;
    public Ease mode;
    public boolean usePingPong;

    public TweenComponent(final Duration duration) {
        this(duration, Ease.LINEAR_OUT);
    }

    public TweenComponent(final Duration duration, final Ease mode) {
        this(duration, mode, false);
    }

    public TweenComponent(final Duration duration, final Ease mode, final boolean isLooped) {
        this(duration, mode, isLooped, true);
    }

    public TweenComponent(final Duration duration, final Ease mode, final boolean isLooped, final  boolean usePingPong) {
        this.duration = duration;
        this.isLooped = isLooped;
        this.mode = mode;
        this.usePingPong = usePingPong;
    }
}