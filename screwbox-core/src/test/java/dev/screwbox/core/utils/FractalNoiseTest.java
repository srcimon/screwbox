package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

class FractalNoiseTest {

    @Test
    void createPreview_validParameters_createsExpectedResult() {
        var result = FractalNoise.createPreview(Color.WHITE, Size.square(128), 50, 139234L);

        verifyIsSameImage(result.image(), "utils/FractalNoise_preview.png");
    }

    @Test
    void createPreview_colorNull_throwsException() {
        Size size = Size.square(128);
        assertThatThrownBy(() -> FractalNoise.createPreview(null, size, 50, 139234L))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("color must not be null");
    }

    @Test
    void createPreview_sizeInvalid_throwsException() {
        Size size = Size.of(0, 10);
        assertThatThrownBy(() -> FractalNoise.createPreview(Color.RED, size, 50, 139234L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("invalid size for preview image");
    }

    @Test
    void createPreview_zoomIsZero_throwsException() {
        Size size = Size.of(4, 10);
        assertThatThrownBy(() -> FractalNoise.createPreview(Color.RED, size, 0, 139234L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("zoom must be positive");
    }

    @Test
    void generateFractalNoise_validInputs_createsReproducibleNoise() {
        var result = FractalNoise.generateFractalNoise(10, 123019, Offset.at(10, 49));
        var second = FractalNoise.generateFractalNoise(10, 123019, Offset.at(10, 49));

        assertThat(result.value()).isEqualTo(0.42, offset(0.01));
        assertThat(result).isEqualTo(second);
    }
}
