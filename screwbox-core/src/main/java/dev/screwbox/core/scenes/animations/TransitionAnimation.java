package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;

import java.awt.*;

/**
 * An animation played when leaving or entering a {@link dev.screwbox.core.scenes.Scene}. Works similar to
 * {@link PostProcessingFilter} but also uses progress value to specify animation position.
 *
 * @since 3.26.0
 */
@FunctionalInterface
public interface TransitionAnimation {

    /**
     * Applies the animation using the current screen (source) to the target graphics.
     *
     * @param source   current screen image
     * @param target   target graphics
     * @param size     size of the screen
     * @param progress progress of the animation
     */
    void apply(Image source, Graphics2D target, Size size, Percent progress);
    //TODO AnimationContext: size, resolutionscale, progress
    //TODO add resoultionscale to postfilter
}
