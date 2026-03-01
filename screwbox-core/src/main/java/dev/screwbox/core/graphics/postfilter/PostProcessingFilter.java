package dev.screwbox.core.graphics.postfilter;

import java.awt.*;

/**
 * Used to create post processing graphics effects. Uses default java API for drawing to add max flexiblity and reduce
 * all overhead, because these filters must be lightning fast.
 *
 * @since 3.24.0
 */
//TODO reference guide
@FunctionalInterface
public interface PostProcessingFilter {

    /**
     * Applies the filter and copies content from source to target. Must only copy contents that resides in {@link PostProcessingContext#bounds()}.
     *
     * @param source  source image that is used for input of the filter
     * @param target  graphics object of the target image. Target images has same size as source.
     * @param context context information to customize filter.
     */
    void apply(Image source, Graphics2D target, PostProcessingContext context);
}
