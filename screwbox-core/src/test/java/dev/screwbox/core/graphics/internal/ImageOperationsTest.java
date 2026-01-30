package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageOperationsTest {

    private static final Image SOME_IMAGE = Frame.fromFile("tile.bmp").image();

    @Test
    void invertOpacity_wrongType_throwsException() {
        var image = Frame.fromFile("transparent.png").image();

        assertThatThrownBy(() -> ImageOperations.invertOpacity(image))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("image type not supported: 6");
    }

    @Test
    void invertOpacity_supportedType_invertsOpacity() {
        var image = Frame.fromFile("transparent.png").image();
        assertThat(Frame.fromImage(image).colorAt(0,0)).isEqualTo(Color.TRANSPARENT);

        var supportedImage = ImageOperations.cloneImage(image);

        ImageOperations.invertOpacity(supportedImage);

        assertThat(Frame.fromImage(supportedImage).colorAt(0,0)).isEqualTo(Color.BLACK);
    }

    @Test
    void toBufferedImage_alreadyABufferedImage_createsClone() {
        BufferedImage result = ImageOperations.cloneImage(SOME_IMAGE);

        assertThat(result).isNotEqualTo(SOME_IMAGE);
    }

    @ParameterizedTest
    @CsvSource({"10,40", "40,195", "190,190"})
    void stackImages_distinctSizes_throwsException(int width, int height) {
        BufferedImage top = ImageOperations.createImage(Size.of(40, 190));
        BufferedImage bottom = ImageOperations.createImage(Size.of(width, height));

        assertThatThrownBy(() -> ImageOperations.stackImages(top, bottom))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("images must have same size");
    }
}