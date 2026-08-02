package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.smoke.styles.TrueColorSmokeStyle;
import dev.screwbox.core.test.TestUtil;
import dev.screwbox.core.utils.FractalNoise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

class SmokeRendererTest {

    static {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    @DisabledOnOs({OS.MAC, OS.WINDOWS}) // headless image is different from os compatible image
    void renderSmoke_noScaleTrueColorStyle_createsSmokeImage() {
        FluidSimulation simulation = new FluidSimulation(16);
        for (final var cell : Size.square(simulation.resolution()).all()) {
            final var noise = FractalNoise.generateFractalNoise(15.0, 13213L, cell);
            Color color = Color.rgb(
                noise.rangeValue(0, 255),
                noise.rangeValue(0, 255),
                noise.rangeValue(0, 255),
                noise);
            simulation.addDensity(cell, 0.004, color);
        }

        //TODO cleanup and add tests

        var image = new SmokeRenderer().renderSmoke(simulation.densityData(), new ScreenBounds(Size.square(simulation.resolution())), 1, new TrueColorSmokeStyle());
        TestUtil.verifyIsSameImage(image, "smoke/renderSmoke_noScaleTrueColorStyle_createsSmokeImage.png");
    }
}
