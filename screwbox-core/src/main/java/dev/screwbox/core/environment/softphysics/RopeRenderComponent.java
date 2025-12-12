package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

import java.io.Serial;

/**
 * Renders ropes when added to entity also containing a {@link RopeComponent}.
 *
 * @since 3.16.0
 */
public class RopeRenderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Stroke width used for drawing.
     */
    public int strokeWidth;

    /**
     * Color used for drawing.
     */
    public Color color;

    /**
     * Draw order used for rendering.
     */
    public int drawOrder;

    public RopeRenderComponent(final Color color, final int strokeWidth) {
        this.color = color;
        this.strokeWidth = strokeWidth;
    }
}
