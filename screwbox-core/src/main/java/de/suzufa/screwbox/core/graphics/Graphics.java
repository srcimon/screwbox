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
     */
    double updateCameraZoom(double zoom);

    Graphics updateCameraPosition(Vector position);

    default Graphics moveCameraBy(final Vector delta) {
        return updateCameraPosition(cameraPosition().add(delta));
    }

    Vector cameraPosition();

    double cameraZoom();

    Vector screenToWorld(Offset offset);

    List<Dimension> supportedResolutions();

    List<Dimension> supportedResolutions(AspectRatio ratio);

    List<String> availableFonts();

}