package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

import java.io.Serial;

/**
 * Links tweening to the opacity of the {@link RenderComponent} of an {@link Entity}.
 */
public class TweenOpacityComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Percent from;
    public Percent to;

    public TweenOpacityComponent() {
        this(Percent.min(), Percent.max());
    }

    public TweenOpacityComponent(final Percent from, final Percent to) {
        this.from = from;
        this.to = to;
    }
}
