package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;

import java.io.Serial;

public class ParticleCustomizeTweenComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Duration timeToLive;
    public final Percent abrevation;
    public final TweenMode mode;

    public ParticleCustomizeTweenComponent(final Duration duration, final TweenMode mode) {
        this(duration, Percent.zero(), mode);
    }

    public ParticleCustomizeTweenComponent(final Duration duration, final Percent durationAbrevation) {
        this(duration, durationAbrevation, TweenMode.LINEAR_IN);
    }

    public ParticleCustomizeTweenComponent(final Duration duration, final Percent durationAbrevation, final TweenMode mode) {
        this.timeToLive = duration;
        this.abrevation = durationAbrevation;
        this.mode = mode;
    }
}
