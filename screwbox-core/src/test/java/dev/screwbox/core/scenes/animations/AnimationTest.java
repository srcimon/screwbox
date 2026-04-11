package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.ImageOperations;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract class AnimationTest {

    protected BufferedImage targetImage;
    protected Graphics2D target;
    protected Image source;
    protected Size size;

    @BeforeEach
    void setUp() {
        source = SpriteBundle.SHADER_PREVIEW.get().singleImage();
        size = Size.of(source.getWidth(null), source.getHeight(null));
        targetImage = ImageOperations.createImage(size);
        target = targetImage.createGraphics();
    }


    @AfterEach
    void tearDown() {
        target.dispose();
    }
}
