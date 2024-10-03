package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Ease;
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
    public Percent progress = Percent.zero();
    public Percent value = Percent.zero();
    public boolean reverse = false;

    public Duration duration;
    public boolean isLooped;
    public Ease mode;
    public boolean usePingPong;
//TODO change tween loopt to default true
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