package io.github.simonbas.screwbox.core.graphics.internal;

import io.github.simonbas.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageUtilTest {

    private static final Image SOME_IMAGE = Frame.fromFile("tile.bmp").image();

    @Test
    void scale_sizeTooSmall_throwsException() {
        assertThatThrownBy(() -> ImageUtil.scale(SOME_IMAGE, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scaled image is size is invalid");
    }

    @Test
    void scale_sizeValid_returnsNewImage() {
        Image result = ImageUtil.scale(SOME_IMAGE, 4);

        assertThat(result.getWidth(null)).isEqualTo(64);
        assertThat(result.getHeight(null)).isEqualTo(64);
    }

}