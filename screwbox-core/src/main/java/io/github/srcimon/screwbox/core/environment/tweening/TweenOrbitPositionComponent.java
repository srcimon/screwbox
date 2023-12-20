package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

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
