package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.light.ConeLightComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.light.SpotLightComponent;

import java.io.Serial;

/**
 * Links tweening to the opacity of the {@link SpotLightComponent}, {@link GlowComponent}, {@link PointLightComponent}
 * and {@link ConeLightComponent} of an {@link Entity}.
 */
public class TweenLightComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Percent from;
    public Percent to;

    public TweenLightComponent() {
        this(Percent.zero(), Percent.max());
    }

    public TweenLightComponent(final Percent from, final Percent to) {
        this.from = from;
        this.to = to;
    }
}
