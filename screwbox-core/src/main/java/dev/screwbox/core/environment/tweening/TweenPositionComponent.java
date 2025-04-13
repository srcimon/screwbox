package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;

import java.io.Serial;

/**
 * Links tweening to the position of the {@link TransformComponent} of an {@link Entity}.
 */
public class TweenPositionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector from;
    public Vector to;

    public TweenPositionComponent(final Vector from, final Vector to) {
        this.from = from;
        this.to = to;
    }
}
