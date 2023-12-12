package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

//TODO: javadoc and tests
public class TweenStateComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Time startTime = Time.now();
    public Percent progress = Percent.min();
    public long cycleCount = 0;
    public boolean reverse;
    public Duration duration;
    public boolean isLooped;

    public TweenStateComponent(final Duration duration, final boolean isLooped) {
        this(duration, isLooped, false);
    }

    public TweenStateComponent(final Duration duration, final boolean isLooped, boolean reverse) {
        this.duration = duration;
        this.isLooped = isLooped;
        this.reverse = reverse;
    }
}
