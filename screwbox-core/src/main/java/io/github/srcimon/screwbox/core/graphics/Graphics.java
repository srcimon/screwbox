package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.loop.Loop;

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
     * Access drawing operations on the game world. So you don't have to use a calculator to draw on the right postion
     * on the {@link Screen}.
     *
     * @see #screen()
     */
    World world();

    /**
     * Get information abound the {@link Camera} like {@link Camera#position()}. Change {@link Camera} settings like {@link Camera#zoom()}.
     */
    Camera camera();

    /**
     * Subsystem for creating and rendering light effects to the screen.
     */
    Light light();

    /**
     * Returns the position the given {@link Offset} in the {@link World} not considering {@link Screen#rotation()}.
     */
    Vector toPosition(Offset offset);

    /**
     * Returns the {@link ScreenBounds} of the {@link Bounds} in the {@link World}.
     */
    ScreenBounds toViewport(Bounds bounds);

    /**
     * Retruns the corresponding {@link ScreenBounds} of the specified {@link Bounds} using a parallax-effect.
     */
    ScreenBounds toScreenUsingParallax(Bounds bounds, double parallaxX, double parallaxY);

    /**
     * Returns the {@link Offset} on the viewport of the given {@link Vector} in the {@link World}.
     *
     * @param position the position that will be translated
     * @return the {@link Offset} on the viewport
     */
    Offset toViewport(Vector position);

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

    /**
     * Returns the duration of the last rendering on the screen. Rendering happens asynchronus to the main {@link Loop} and
     * will only slow down the {@link Engine} if the {@link Duration} is longer than the update of all other engine components.
     */
    Duration renderDuration();

}