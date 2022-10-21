package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.graphics.Color;

public class GlowLightComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final double size;
    public final Color color;

    public GlowLightComponent(final double size, final Color color) {
        this.size = size;
        this.color = color;
    }

    public GlowLightComponent(final double size) {
        this(size, Color.WHITE.withOpacity(0.75));
    }

}
