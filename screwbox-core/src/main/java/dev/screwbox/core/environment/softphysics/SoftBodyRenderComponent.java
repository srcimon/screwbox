package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

public class SoftBodyRenderComponent implements Component {

    public Color color;
    public int drawOrder = 0;
    public Color outlineColor = Color.TRANSPARENT;
    public int outlineStrokeWidth = 2;

    public SoftBodyRenderComponent(final Color color) {
        this.color = color;
    }
}
