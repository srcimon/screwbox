package dev.screwbox.core.graphics;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.filter.PostProcessingFilter;
import dev.screwbox.core.graphics.options.ShockwaveOptions;

public interface PostProcessing {

    PostProcessing triggerShockwave(Vector position, ShockwaveOptions options);

    PostProcessing addFilter(PostProcessingFilter filter);

    PostProcessing addViewportFilter(PostProcessingFilter filter);

    PostProcessing clearFilters();

    //TODO int filterCount();
    //TODO boolean isActive();
    //TODO PostProcessing removeFilter(Class<? extends PostProcessingFilter type);
}
