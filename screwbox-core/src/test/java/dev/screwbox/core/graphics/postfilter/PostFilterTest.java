package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.graphics.Offset;
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

import java.awt.*;
import java.awt.image.BufferedImage;

abstract class PostFilterTest {

    protected BufferedImage targetImage;
    protected Graphics2D target;
    protected Viewport viewport;
    protected Image source;

    @BeforeEach
    void setUp() {
        source = SpriteBundle.SHADER_PREVIEW.get().singleImage();
        final Size size = Size.of(source.getWidth(null), source.getHeight(null));
        targetImage = ImageOperations.createImage(size);
        target = targetImage.createGraphics();
        DefaultCanvas canvas = new DefaultCanvas(new DefaultRenderer(), new ScreenBounds(Offset.origin(), size));
        viewport = new DefaultViewport(canvas, new DefaultCamera(canvas));
    }


    @AfterEach
    void tearDown() {
        target.dispose();
    }
}
