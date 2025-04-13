package io.github.srcimon.screwbox.platformer.components;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class BackgroundComponent implements Component {

    @Serial
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
