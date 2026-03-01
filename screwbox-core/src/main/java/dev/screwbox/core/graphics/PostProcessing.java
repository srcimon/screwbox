package dev.screwbox.core.graphics;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.options.ShockwaveOptions;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;

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

    /**
     * Returns the count of currently active shockwaves.
     *
     * @see #triggerShockwave(Vector, ShockwaveOptions)
     */
    int shockwaveCount();

    /**
     * Adds a filter that is applied on the whole {@link Screen}.
     * Note that every filter added will slow down the rendering massivly.
     */
    PostProcessing addScreenFilter(PostProcessingFilter filter);

    /**
     * Adds a filter that is applied on every {@link Viewport} of a split screen or the whole {@link Screen} if split screen
     * mode is not active.
     * Note that every filter added will slow down the rendering massivly.
     */
    PostProcessing addViewportFilter(PostProcessingFilter filter);

    /**
     * Removes all previously added filters. Does not remove active shockwaves.
     */
    PostProcessing clearFilters();

    //TODO PostProcessing addViewportFilter(int viewportId, PostProcessingFilter filter);

    //TODO int filterCount();
    //TODO boolean isActive();
    PostProcessing removeFilter(Class<? extends PostProcessingFilter> filter);
}
