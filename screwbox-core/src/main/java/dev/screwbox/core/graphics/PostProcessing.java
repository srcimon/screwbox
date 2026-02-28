package dev.screwbox.core.graphics;

import dev.screwbox.core.graphics.effects.PostProcessingFilter;

public interface PostProcessing {

    PostProcessing addFilter(PostProcessingFilter effect);

    PostProcessing clearFilters();
}
