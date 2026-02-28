package dev.screwbox.core.graphics;

import dev.screwbox.core.graphics.filter.PostProcessingFilter;

public interface PostProcessing {

    PostProcessing addFilter(PostProcessingFilter effect);

    PostProcessing clearFilters();
}
