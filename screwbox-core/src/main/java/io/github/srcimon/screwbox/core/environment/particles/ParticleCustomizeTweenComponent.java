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
    public final TweenMode mode = TweenMode.LINEAR_IN; //TODO add to constructor

    public ParticleCustomizeTweenComponent(final Duration timeToLive) {
        this.timeToLive = timeToLive;
        this.abrevation = Percent.zero();
    }

    public ParticleCustomizeTweenComponent(final Duration timeToLive, final Percent abrevation) {
        this.timeToLive = timeToLive;
        this.abrevation = abrevation;
    }
}
