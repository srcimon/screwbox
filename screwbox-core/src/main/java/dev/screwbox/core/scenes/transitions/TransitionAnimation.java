package dev.screwbox.core.scenes.transitions;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
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
     * Applies the animation and copies content from source to target.
     *
     * @param source  source image that is used for input of the filter
     * @param target  graphics object of the target image. Target images has same size as source.
     * @param context context information to customize filter.
     */
    void apply(Image source, Graphics2D target, PostProcessingContext context, Percent progress);

    //TODO allow overdrawing last scene for all transitions
    default void drawSourceImage(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();

        target.drawImage(source,
            area.x(), area.y(), area.maxX(), area.maxY(),
            area.x(), area.y(), area.maxX(), area.maxY(),
            null);
    }
}
