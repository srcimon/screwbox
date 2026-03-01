package dev.screwbox.core.graphics;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.filter.PostProcessingFilter;
import dev.screwbox.core.graphics.options.ShockwaveOptions;

/**
 * Add {@link PostProcessingFilter} to create optical effects on the {@link Screen}. Also provides some easy-to-use
 * functions to create more sofisticated optical effects like {@link #triggerShockwave(Vector, ShockwaveOptions)}.
 *
 * @since 3.24.0
 */
public interface PostProcessing {

    /**
     * Creates a shockwave effect using an internal {@link PostProcessingFilter}. The effect
     * is purly optical and does not affect physics {@link Entity entities}.
     */
    PostProcessing triggerShockwave(Vector position, ShockwaveOptions options);

    int shockwaveCount();

    PostProcessing addFilter(PostProcessingFilter filter);

    PostProcessing addViewportFilter(PostProcessingFilter filter);

    //TODO PostProcessing addViewportFilter(int viewportId, PostProcessingFilter filter);

    PostProcessing clearFilters();

    //TODO int filterCount();
    //TODO boolean isActive();
    //TODO PostProcessing removeFilter(Class<? extends PostProcessingFilter type);
}
