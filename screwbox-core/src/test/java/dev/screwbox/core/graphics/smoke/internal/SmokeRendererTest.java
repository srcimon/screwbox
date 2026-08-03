package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.smoke.styles.ComicSmokeStyle;
import dev.screwbox.core.graphics.smoke.styles.FrostSmokeStyle;
import dev.screwbox.core.graphics.smoke.styles.HeatVisionSmokeStyle;
import dev.screwbox.core.graphics.smoke.styles.TrueColorSmokeStyle;
import dev.screwbox.core.utils.FractalNoise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThat;

@DisabledOnOs({OS.MAC, OS.WINDOWS})// headless image is different from os compatible one
class SmokeRendererTest {

    DensityInfo densityInfo;

    @BeforeEach
    void setup() {
        System.setProperty("java.awt.headless", "true");
        final var simulation = new FluidSimulation(16);
        for (final var cell : Size.square(simulation.resolution()).all()) {
            simulation.addDensity(cell, 0.004, Color.rgb(
                FractalNoise.generateFractalNoise(15.0, 13213L, cell).rangeValue(0, 255),
                FractalNoise.generateFractalNoise(15.0, 14513L, cell).rangeValue(0, 255),
                FractalNoise.generateFractalNoise(15.0, 63413L, cell).rangeValue(0, 255),
                FractalNoise.generateFractalNoise(15.0, 12313L, cell)));
        }

        densityInfo = simulation.densityData();
    }

    @Test
    void renderSmoke_noScaleTrueColorStyle_createsSmokeImage() {
        var image = new SmokeRenderer().renderSmoke(densityInfo, new ScreenBounds(Size.square(16)), 1, new TrueColorSmokeStyle());
        verifyIsSameImage(image, "smoke/renderSmoke_noScaleTrueColorStyle_createsSmokeImage.png");
    }

    @Test
    void renderSmoke_scaledComicStyle_createsSmokeImage() {
        var image = new SmokeRenderer().renderSmoke(densityInfo, new ScreenBounds(Size.square(16)), 2, new ComicSmokeStyle());
        verifyIsSameImage(image, "smoke/renderSmoke_scaledComicStyle_createsSmokeImage.png");
    }

    @Test
    void renderSmoke_noScaleFrostStyle_createsSmokeImage() {
        var image = new SmokeRenderer().renderSmoke(densityInfo, new ScreenBounds(Size.square(16)), 1, new FrostSmokeStyle());
        verifyIsSameImage(image, "smoke/renderSmoke_noScaleFrostStyle_createsSmokeImage.png");
    }

    @Test
    void renderSmoke_noScaleHeatVisionStyle_createsSmokeImage() {
        var image = new SmokeRenderer().renderSmoke(densityInfo, new ScreenBounds(Size.square(16)), 1, new HeatVisionSmokeStyle());

        verifyIsSameImage(image, "smoke/renderSmoke_noScaleHeatVisionStyle_createsSmokeImage.png");
    }

    @Test
    void renderSmoke_twice_returnsCachedImage() {
        SmokeRenderer smokeRenderer = new SmokeRenderer();

        var image = smokeRenderer.renderSmoke(densityInfo, new ScreenBounds(Size.square(16)), 1, new HeatVisionSmokeStyle());
        var secondImage = smokeRenderer.renderSmoke(densityInfo, new ScreenBounds(Size.square(16)), 1, new HeatVisionSmokeStyle());

        assertThat(image).isEqualTo(secondImage);
    }
}
