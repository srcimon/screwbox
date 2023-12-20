package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

import java.io.Serial;

/**
 * Links tweening to the y-position of the {@link TransformComponent} of an {@link Entity}.
 */
public class TweenYPositionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double from;
    public double to;

    public TweenYPositionComponent(final double from, final double to) {
        this.from = from;
        this.to = to;
    }
}
