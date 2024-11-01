package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.loop.Loop;

import java.util.List;
import java.util.Optional;

/**
 * Gives access to all graphics related configuration and operations.
 */
public interface Graphics extends Viewport {

    //TODO add javadoc
    Graphics enableSplitscreenMode(SplitScreenOptions options);

    //TODO add javadoc
    Graphics disableSplitscreenMode();

    //TODO add javadoc
    boolean isSplitscreenModeEnabled();

    //TODO add javadoc
    Optional<Viewport> vieport(int index);

    //TODO add javadoc
    List<Viewport> viewports();

    //TODO add javadoc
    Viewport primaryViewport();

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
     * Creates a {@link Canvas} that can be used to draw only on a specific area on the {@link Screen} without
     * paying attention to calculating relative {@link Canvas#offset()} for every drawing task.
     */
    Canvas createCanvas(Offset offset, Size size);

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

    /**
     * Checks if the specified position is within the specified distance to the visible area. This also works in split screen.
     */
    boolean isWithinDistanceToVisibleArea(Vector position, double distance);

}