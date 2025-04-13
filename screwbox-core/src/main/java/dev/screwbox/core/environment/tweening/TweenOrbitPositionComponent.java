package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;

import java.io.Serial;

/**
 * Links tweening to the position of the {@link TransformComponent} of an {@link Entity} around a center position.
 */
public class TweenOrbitPositionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Vector center;
    public final double distance;

    public TweenOrbitPositionComponent(final Vector center, final double distance) {
        this.center = center;
        this.distance = distance;
    }
}
