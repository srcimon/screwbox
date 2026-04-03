package dev.screwbox.core.scenes.animations;

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
     * @param source  current screen image
     * @param target  target graphics
     * @param context context of the current animation
     */
    void apply(Image source, Graphics2D target, AnimationContext context);
}
