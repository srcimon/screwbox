package dev.screwbox.core.graphics;

import dev.screwbox.core.graphics.filter.PostProcessingFilter;

public interface PostProcessing {

    PostProcessing addFilter(PostProcessingFilter filter);

    PostProcessing addViewportFilter(PostProcessingFilter filter);

    PostProcessing clearFilters();

    //TODO int filterCount();
    //TODO boolean isActive();
    //TODO PostProcessing removeFilter(Class<? extends PostProcessingFilter type);
}
