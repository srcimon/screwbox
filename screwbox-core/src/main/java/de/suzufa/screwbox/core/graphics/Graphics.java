package de.suzufa.screwbox.core.graphics;

import java.util.List;

import de.suzufa.screwbox.core.Vector;

public interface Graphics {

    GraphicsConfiguration configuration();

    Window window();

    World world();

    /**
     * Updates the camera zoom nearly to the given value. The actual zoom value may
     * be slightly different to avoid graphic glitches because of floating point
     * imprecisions. The actual zoom value is returned.
     * 
     * @param zoom the zoom value that should be applied
     * @return the zoom value that was applied
     * 
     * @see #updateCameraZoomBy(double)
     */
    double updateCameraZoom(double zoom);

    Graphics updateCameraPosition(Vector position);

    /**
     * Updates the camera zoom nearly by the given value. The actual zoom value may
     * be slightly different to avoid graphic glitches because of floating point
     * imprecisions. The actual zoom value is returned.
     * 
     * @see #updateCameraZoom(double)
     */
    double updateCameraZoomBy(double delta);

    /**
     * Moves the camera position by the given {@link Vector}.
     */
    default Graphics moveCameraBy(final Vector delta) {
        return updateCameraPosition(cameraPosition().add(delta));
    }

    /**
     * Restricts zooming to the given range. Default min zoom is 0.5 and max is 10.
     */
    Graphics restrictZoomRangeTo(double min, double max);

    Vector cameraPosition();

    double cameraZoom();

    /**
     * Determins the position the given Window {@link Offset} in the game
     * {@link World}.
     */
    Vector worldPositionOf(Offset offset);

    List<Dimension> supportedResolutions();

    List<Dimension> supportedResolutions(AspectRatio ratio);

    /**
     * Returns the current screen resolution.
     */
    Dimension currentResolution();

    List<String> availableFonts();

}