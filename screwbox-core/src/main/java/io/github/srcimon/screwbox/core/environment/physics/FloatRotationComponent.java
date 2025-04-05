package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

import java.io.Serial;

/**
 * Adjusts rotation of {@link RenderComponent} to {@link FloatComponent#attachedWave}.
 *
 * @since 2.19.0
 */
public class FloatRotationComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double adjustmentSpeed = 6;
}
