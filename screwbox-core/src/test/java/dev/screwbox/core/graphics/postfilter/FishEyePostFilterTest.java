package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.DefaultCamera;
import dev.screwbox.core.graphics.internal.DefaultCanvas;
import dev.screwbox.core.graphics.internal.DefaultViewport;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.mockito.Mockito.when;


class FishEyePostFilterTest {

    BufferedImage targetImage;
    Graphics2D target;

    @BeforeEach
    void setUp() {
        targetImage = ImageOperations.createImage(Size.square(40));
        target =  targetImage.createGraphics();
    }
    //TODO fixup this test
    @Test
    void xx() {
        var filter = new FishEyePostFilter(10, -0.8);
        DefaultCanvas canvas = new DefaultCanvas(new DefaultRenderer(), new ScreenBounds(0, 0, 40, 40));
        Viewport mock = new DefaultViewport(canvas, new DefaultCamera(canvas));
        PostProcessingContext context = new PostProcessingContext(Color.BLACK, Time.now(), Duration.ofMillis(10), mock);
        filter.apply(SpriteBundle.SHADER_PREVIEW.get().singleImage(), target, context);

        Frame.fromImage(targetImage).exportPng("demo.png");
    }

    @AfterEach
    void tearDown() {
        target.dispose();
    }
}
