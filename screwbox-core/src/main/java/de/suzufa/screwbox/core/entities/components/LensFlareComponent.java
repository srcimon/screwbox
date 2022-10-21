package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.graphics.Color;

public class LensFlareComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final double size;
    public final Color color;

    public LensFlareComponent(final double size, final Color color) {
        this.size = size;
        this.color = color;
    }

    public LensFlareComponent(final double size) {
        this(size, Color.WHITE);
    }

}
