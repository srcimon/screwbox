package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.options.ShockwaveOptions;
import dev.screwbox.core.graphics.postfilter.FishEyePostFilter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class DefaultPostprocessingTest {

    @InjectMocks
    DefaultPostProcessing postProcessing;

    @Test
    void isActive_noFilterAndShockwaves_isFalse() {
        assertThat(postProcessing.isActive()).isFalse();
    }

    @Test
    void isActive_shockwaveTriggered_isTrue() {
        postProcessing.triggerShockwave($(40, 10), ShockwaveOptions.radius(20));

        assertThat(postProcessing.isActive()).isTrue();
    }

    @Test
    void isActive_screenFilterPresent_isTrue() {
        postProcessing.addScreenFilter(new FishEyePostFilter(16, 1.0));

        assertThat(postProcessing.isActive()).isTrue();
    }

    @Test
    void clearFilters_filterPresent_removesFilter() {
        postProcessing.addScreenFilter(new FishEyePostFilter(16, 1.0));

        postProcessing.clearFilters();

        assertThat(postProcessing.isActive()).isFalse();
        assertThat(postProcessing.filterCount()).isZero();
    }

    @Test
    void addScreenFilter_increasesFilterCount_addsFilter() {
        postProcessing.addScreenFilter(new FishEyePostFilter(16, 1.0));

        assertThat(postProcessing.filterCount()).isOne();
    }

    @Test
    void addViewportFilter_increasesFilterCount_addsFilter() {
        postProcessing.addViewportFilter(new FishEyePostFilter(16, 1.0));

        assertThat(postProcessing.filterCount()).isOne();
    }
}
