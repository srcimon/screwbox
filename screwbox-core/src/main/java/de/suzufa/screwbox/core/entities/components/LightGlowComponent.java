package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.graphics.Color;

public class LightGlowComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final double size;
    public final Color color;

    public LightGlowComponent(final double size, final Color color) {
        this.size = size;
        this.color = color;
    }

    public LightGlowComponent(final double size) {
        this(size, Color.WHITE.withOpacity(0.05));
    }

}
