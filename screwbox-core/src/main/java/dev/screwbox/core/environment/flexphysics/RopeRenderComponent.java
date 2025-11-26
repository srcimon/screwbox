package dev.screwbox.core.environment.flexphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

public class RopeRenderComponent implements Component {

    public int strokeWidth;
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
