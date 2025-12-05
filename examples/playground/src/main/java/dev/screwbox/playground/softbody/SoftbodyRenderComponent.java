package dev.screwbox.playground.softbody;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

public class SoftbodyRenderComponent implements Component {

    public Color color;
    public Color outlineColor = Color.TRANSPARENT;
    public int outlineStrokeWidth = 2;

    public SoftbodyRenderComponent(final Color color) {
        this.color = color;
    }
}
