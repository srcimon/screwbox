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

    /**
     * Start {@link Time} of the tween.
     */
    public Time startTime = Time.now();

    /**
     * Current progress of the tween. Will be automatically updated.
     */
    public Percent progress = Percent.zero();

    /**
     * {@link Ease} used to calculate {@link #value} from current {@link #progress}.
     */
    public Ease ease;

    /**
     * Value that is used by other tween components.
     */
    public Percent value = Percent.zero();

    /**
     * Tween animation runs in reverse mode or not.
     */
    public boolean reverse = false;

    /**
     * Duration of one tween cycle.
     */
    public Duration duration;

    /**
     * Will the tween run in a loop or only once.
     */
    public boolean isLooped;

    /**
     * Will the tween value run up and down again or will it start from zero.
     */
    public boolean usePingPong = true;

    /**
     * Creates a new single cycle tween using the specified duration.
     */
    public TweenComponent(final Duration duration) {
        this(duration, Ease.LINEAR_OUT);
    }

    /**
     * Creates a new single cycle tween using the specified duration and ease.
     */
    public TweenComponent(final Duration duration, final Ease ease) {
        this(duration, ease, false);
    }

    /**
     * Creates a new tween using the specified duration and ease.
     */
    public TweenComponent(final Duration duration, final Ease ease, final boolean isLooped) {
        this.duration = duration;
        this.isLooped = isLooped;
        this.ease = ease;
    }
}