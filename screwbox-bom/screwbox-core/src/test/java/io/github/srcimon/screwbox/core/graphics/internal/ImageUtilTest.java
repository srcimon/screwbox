package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;

class ImageUtilTest {

    private static final Image SOME_IMAGE = Frame.fromFile("tile.bmp").image();

    @Test
    void toBufferedImage_alreadyABufferedImage_noConversion() {
        BufferedImage result = ImageUtil.toBufferedImage(SOME_IMAGE);

        assertThat(result).isEqualTo(SOME_IMAGE);
    }
}