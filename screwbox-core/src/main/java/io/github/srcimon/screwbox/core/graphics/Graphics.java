package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.window.Window;

import java.util.List;

/**
 * Gives access to all graphics related configuration and operations.
 */
public interface Graphics {

    /**
     * Read and change the current {@link GraphicsConfiguration}. All changes take effect right away.
     */
    GraphicsConfiguration configuration();

    /**
     * Access drawing operations on the game screen.
     *
     * @see #world()
     */
    Screen screen();

    /**
     * Access drawing operations on the game world. So you don't have to use a calculator to draw on the right poisition on the {@link Screen} (;
     *
     * @see #screen()
     */
    World world();

    /**
     * Subsystem for creating and rendering light effects to the screen.
     */
    Light light();

    /**
     * Updates the camera zoom nearly to the given value. The actual zoom value may
     * be slightly different to avoid graphic glitches because of floating point
     * imprecisions. The actual zoom value is returned.
     *
     * @param zoom the zoom value that should be applied
     * @return the zoom value that was applied
     * @see #updateZoomRelative(double)
     */
    double updateZoom(double zoom);

    Graphics updateCameraPosition(Vector position);

    /**
     * Updates the camera zoom nearly by the given value. The actual zoom value may
     * be slightly different to avoid graphic glitches because of floating point
     * imprecisions. The actual zoom value is returned.
     *
     * @see #updateZoom(double)
     */
    double updateZoomRelative(double delta);

    /**
     * Moves the camera position by the given {@link Vector}.
     */
    default Graphics moveCamera(final Vector delta) {
        return updateCameraPosition(cameraPosition().add(delta));
    }

    /**
     * Restricts zooming to the given range. Default min zoom is 0.5 and max is 10.
     */
    Graphics restrictZoomRangeTo(double min, double max);

    Vector cameraPosition();

    /**
     * Returns the currently used camera zoom.
     */
    double cameraZoom();

    /**
     * Returns the position the given {@link Offset} in the game
     * {@link World}.
     */
    Vector toPosition(Offset offset);

    /**
     * Returns the {@link Offset} on the {@link Window} of the given {@link Vector}
     * in the game {@link World}.
     *
     * @param position the position that will be translated
     * @return the {@link Offset} on the {@link Window}
     */
    Offset toOffset(Vector position);

    /**
     * Returns a list of all supported resolutions.
     *
     * @see #supportedResolutions(AspectRatio)
     */
    List<Size> supportedResolutions();

    /**
     * Returns a list of all supported resolutions of the given {@link AspectRatio}.
     *
     * @see #supportedResolutions()
     */
    List<Size> supportedResolutions(AspectRatio ratio);

    /**
     * Returns the current screen resolution.
     */
    Size currentResolution();

    /**
     * Returns a list of all font names that can were found on the current system.
     */
    List<String> availableFonts();

}