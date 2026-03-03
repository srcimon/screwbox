package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import dev.screwbox.core.graphics.options.ShockwaveOptions;
import dev.screwbox.core.graphics.postfilter.DeepSeeOdyseePostFilter;
import dev.screwbox.core.graphics.postfilter.FishEyePostFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;

import static dev.screwbox.core.Vector.$;
import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MockitoSettings
class DefaultPostprocessingTest {

    DefaultPostProcessing postProcessing;

    @BeforeEach
    void setUp() {
        var configuration = new GraphicsConfiguration();
        DefaultCanvas canvas = new DefaultCanvas(new DefaultRenderer(), new ScreenBounds(0, 0, 40, 40));
        var defaultViewport = new DefaultViewport(canvas, new DefaultCamera(canvas));
        var viewportManager = new ViewportManager(defaultViewport, null);
        postProcessing = new DefaultPostProcessing(configuration, viewportManager, ImageOperations::createImage);
    }

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

    @Test
    void addViewportFilter_filterNull_throwsException() {
        assertThatThrownBy(() -> postProcessing.addViewportFilter(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("filter must not be null");
    }

    @Test
    void addScreenFilterfilterNull_throwsException() {
        assertThatThrownBy(() -> postProcessing.addScreenFilter(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("filter must not be null");
    }

    @Test
    void removeFilter_filterPresent_removesFilter() {
        postProcessing.addScreenFilter(new FishEyePostFilter(16, 1.0));
        postProcessing.addViewportFilter(new FishEyePostFilter(16, 1.0));
        postProcessing.addViewportFilter(new DeepSeeOdyseePostFilter());

        assertThat(postProcessing.filterCount()).isEqualTo(3);

        postProcessing.removeFilter(FishEyePostFilter.class);
        assertThat(postProcessing.filterCount()).isOne();
    }

    @Test
    void applyEffects_filterAndNoShockwaves_appliesFilterOnTarget() {
        var source = SpriteBundle.SHADER_PREVIEW.get().singleImage();
        var targetImage = ImageOperations.createEmptyImageOfSameSize(source);
        var targetGraphics = targetImage.createGraphics();
        postProcessing.applyEffects(source, targetGraphics, new FishEyePostFilter(3, 0.2));

        Frame result = Frame.fromImage(targetImage);
        Frame input = Frame.fromImage(source);
        assertThat(result.colors()).containsAll(input.colors());
        assertThat(result.hasIdenticalPixels(input)).isFalse();
    }

    @Test
    void applyEffects_noFiltersAndNoShockwaves_doesNotDrawUpponTarget() {
        var source = SpriteBundle.SHADER_PREVIEW.get().singleImage();
        var targetImage = ImageOperations.createEmptyImageOfSameSize(source);
        var targetGraphics = targetImage.createGraphics();
        postProcessing.applyEffects(source, targetGraphics, null);

        Frame result = Frame.fromImage(targetImage);
        assertThat(result.colors()).containsExactly(Color.TRANSPARENT);
    }

    @Test
    void applyEffects_shockwaveTriggered_appliesShockwaveFilterOnTarget() {
        var source = SpriteBundle.SHADER_PREVIEW.get().singleImage();
        var targetImage = ImageOperations.createEmptyImageOfSameSize(source);
        var targetGraphics = targetImage.createGraphics();

        postProcessing.triggerShockwave($(4, 4), ShockwaveOptions.radius(20).intensity(83).duration(Duration.ofSeconds(20)));
        postProcessing.update();
        postProcessing.applyEffects(source, targetGraphics, null);

        Frame result = Frame.fromImage(targetImage);
        Frame input = Frame.fromImage(source);
        assertThat(result.colors()).containsAll(input.colors());
        assertThat(result.hasIdenticalPixels(input)).isFalse();
    }

    @Test
    void applyEffects_multipleFilters_appliesAllFilters() {
        var source = SpriteBundle.SHADER_PREVIEW.get().singleImage();
        var targetImage = ImageOperations.createEmptyImageOfSameSize(source);
        var targetGraphics = targetImage.createGraphics();

        postProcessing.addScreenFilter(new FishEyePostFilter(3, -0.7));
        postProcessing.addScreenFilter(new FishEyePostFilter(4, -0.4));
        postProcessing.applyEffects(source, targetGraphics, null);

        verifyIsSameImage(targetImage, "postfilter/applyEffects_multipleFilters_appliesAllFilters.png");
    }
}
