package de.suzufa.screwbox.core.graphics.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.utils.ResourceLoader;

class ImageConverterTest {

    @Test
    void makeColorTransparent_colorNotPresent_doenstMakeAnythingTransparent() throws Exception {
        BufferedImage image = ImageIO.read(ResourceLoader.resourceFile("tile.bmp"));

        Image result = ImageConverter.makeColorTransparent(image, Color.RED);

        assertThat(countTransparentPixels(result)).isZero();
    }

    @Test
    void makeColorTransparent_colorPresent_makesMatchingPixelsTransparent() throws Exception {
        BufferedImage image = ImageIO.read(ResourceLoader.resourceFile("tile.bmp"));

        Image result = ImageConverter.makeColorTransparent(image, Color.rgb(233, 202, 177));

        assertThat(countTransparentPixels(result)).isEqualTo(2);
    }

    @Test
    void colorAt_inBounds_returnsColor() throws Exception {
        BufferedImage image = ImageIO.read(ResourceLoader.resourceFile("tile.bmp"));

        Color color = ImageConverter.colorAt(image, 4, 4);

        assertThat(color).isEqualTo(Color.rgb(199, 155, 119));
    }

    @ParameterizedTest
    @CsvSource({ "-1,1", "100,1", "4,-2", "4,44" })
    void colorAt_positionOutOfBounds_throwsException(int x, int y) throws Exception {
        BufferedImage image = ImageIO.read(ResourceLoader.resourceFile("tile.bmp"));

        assertThatThrownBy(() -> ImageConverter.colorAt(image, x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dimension is out of bounds: " + x + ":" + y);
    }

    private int countTransparentPixels(Image result) {
        BufferedImage analyzableImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        analyzableImage.getGraphics().drawImage(result, 0, 0, null);

        int transparentPixelCount = 0;
        for (int x = 0; x < analyzableImage.getWidth(); x++) {
            for (int y = 0; y < analyzableImage.getHeight(); y++) {
                int pixel = analyzableImage.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                if (alpha == 0) {
                    transparentPixelCount++;
                }
            }
        }

        return transparentPixelCount;
    }
}
