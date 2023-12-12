package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;
//TODO: javadoc and tests
public class TweenState implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Time startTime = Time.now();
    public Percent progress = Percent.min();
    public boolean reverse;
    public Duration duration;
    public int loopCount;

    public TweenState(final Duration duration, final int loopCount) {
        this.duration = duration;
        this.loopCount = loopCount;
    }

    public TweenState(final Duration duration, final int loopCount, boolean reverse) {
        this.duration = duration;
        this.loopCount = loopCount;
        this.reverse = reverse;
    }
}
