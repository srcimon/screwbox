package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;

public interface Viewport {

    /**
     * Returns the {@link Offset} on the viewport of the given {@link Vector} in the {@link World}.
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

    int toCanvas(double worldDistance);

    /**
     * Retruns the corresponding {@link ScreenBounds} of the specified {@link Bounds} using a parallax-effect.
     */
    ScreenBounds toCanvas(Bounds bounds, double parallaxX, double parallaxY);

    Camera camera();

    Canvas canvas();
}
