package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.entities.Component;

public class BackgroundComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final double parallaxX;
    public final double parallaxY;
    public final double zoom;

    public BackgroundComponent(double parallaxX, double parallaxY, double zoom) {
        this.parallaxX = parallaxX;
        this.parallaxY = parallaxY;
        this.zoom = zoom;
    }

}
