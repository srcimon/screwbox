package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class TweenCircleAroundPositionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Vector center;
    public final double distance;

    public TweenCircleAroundPositionComponent(final Vector center, final double distance) {
        this.center = center;
        this.distance = distance;
    }
}
