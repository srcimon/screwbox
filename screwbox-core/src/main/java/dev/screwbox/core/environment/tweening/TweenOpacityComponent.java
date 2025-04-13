package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.rendering.RenderComponent;

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
        this(Percent.zero(), Percent.max());
    }

    public TweenOpacityComponent(final Percent from, final Percent to) {
        this.from = from;
        this.to = to;
    }
}
