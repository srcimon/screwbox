package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.smoke.styles.OriginalColorSmokeStyle;
import dev.screwbox.core.test.TestUtil;
import dev.screwbox.core.utils.FractalNoise;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SmokeRendererTest {

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void xxx() {
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
        var data = simulation.densityData();
//TODO cleanup
        var image = new SmokeRenderer().createImage(data, new ScreenBounds(Size.square(simulation.resolution())), 1, new OriginalColorSmokeStyle());
//        Frame.fromImage(image).exportPng("demo.png");
        TestUtil.verifyIsSameImage(image, "smoke/demo.png");
    }

    @AfterEach
    void tearDown() {
        System.clearProperty("java.awt.headless");
    }
}
