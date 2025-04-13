package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Sprite;

import java.io.Serial;

/**
 * Applies a continuous rotation to the {@link Sprite} of an {@link RenderComponent}.
 */
public class FixedRotationComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double clockwiseRotationsPerSecond;

    public FixedRotationComponent(final double clockwiseRotationsPerSecond) {
        this.clockwiseRotationsPerSecond = clockwiseRotationsPerSecond;
    }
}
