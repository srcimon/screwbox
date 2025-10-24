package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Sprite;

import java.io.Serial;

/**
 * Rotates {@link Sprite sprites} in direction of motion.
 */
public class MotionRotationComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Maximum speed of rotation changes.
     *
     * @since 3.13.0
     */
    public double maxSpeed = 20;

}
