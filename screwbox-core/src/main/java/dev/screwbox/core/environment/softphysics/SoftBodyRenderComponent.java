package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

/**
 * Renders soft bodies when added to entity also containing a {@link SoftBodyComponent}.
 *
 * @since 3.16.0
 */
public class SoftBodyRenderComponent implements Component {

    /**
     * {@link Color} used for drawing the filling. Will disable drawing the filling when set to {@link Color#TRANSPARENT}.
     */
    public Color color;

    /**
     * {@link Color} used for drawing the outline. Will disable drawing the outline when set to {@link Color#TRANSPARENT}.
     */
    public Color outlineColor = Color.TRANSPARENT;

    /**
     * Stroke width used for drawing the outline.
     */
    public int outlineStrokeWidth = 1;

    /**
     * Draw a rounded soft body.
     */
    public boolean rounded = true;

    /**
     * Draw order used for rendering.
     */
    public int drawOrder = 0;

    /**
     * Creates a new instance using the specified {@link SoftBodyRenderComponent#color}.
     */
    public SoftBodyRenderComponent(final Color color) {
        this.color = color;
    }
}
