package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.light.ConeLightComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.light.SpotLightComponent;

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
