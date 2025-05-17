package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.rendering.RenderComponent;

import java.io.Serial;

/**
 * Adjusts rotation of {@link RenderComponent} to {@link FloatComponent#attachedWave}.
 *
 * @see <a href="https://screwbox.dev/docs/guides/dynamic-fluids/">Guide: Dynamic fluids</a>
 * @since 2.19.0
 */
public class FloatRotationComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double adjustmentSpeed = 6;
}
