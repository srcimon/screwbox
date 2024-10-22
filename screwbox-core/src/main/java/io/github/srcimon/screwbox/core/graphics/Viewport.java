package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;

/**
 * Combines a {@link Camera} with a {@link Canvas} and translates coordinates and distances between both of them.
 */
public interface Viewport {

    /**
     * Returns the {@link Offset} on the viewport of the specified {@link Vector} in the {@link World}.
     *
     * @param position the position that will be translated
     * @return the {@link Offset} on the viewport
     */
    Offset toCanvas(Vector position);

    /**
     * Returns the area in the {@link World} currently visible on the {@link Canvas}.
     */
    Bounds visibleArea();

    /**
     * Returns the position of the specified {@link Offset} in the {@link World}.
     */
    Vector toWorld(Offset offset);

    /**
     * Returns the {@link ScreenBounds} of the {@link Bounds} in the {@link World}.
     */
    ScreenBounds toCanvas(Bounds bounds);

    /**
     * Translates a distance in the {@link World} to a distance on the {@link Canvas}.
     *
     * @param distance distance in the {@link World}
     * @return distance on the {@link Canvas}
     */
    int toCanvas(double distance);

    /**
     * Retruns the corresponding {@link ScreenBounds} of the specified {@link Bounds} using a parallax-effect.
     */
    ScreenBounds toCanvas(Bounds bounds, double parallaxX, double parallaxY);

    /**
     * Returns the {@link Camera} associated with the {@link Viewport}.
     */
    Camera camera();

    /**
     * Returns the {@link Canvas} associated withe the {@link Viewport}.
     */
    Canvas canvas();
}
