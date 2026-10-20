package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.ShaderBundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextureLineDrawOptionsTest {

    @Test
    void newInstance_scaleNegative_throwsException() {
        assertThatThrownBy(() -> TextureLineDrawOptions.scaled(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("scale must not be negative (actual value: -1.0)");
    }

    @Test
    void newInstance_opacityIsNull_throwsException() {
        var options = TextureLineDrawOptions.originalSize();

        assertThatThrownBy(() -> options.opacity(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("opacity must not be null");
    }

    @Test
    void newInstance_allPropertiesSet_hasCorrectValues() {
        var options = TextureLineDrawOptions.scaled(3.1)
            .drawOrder(4)
            .shader(ShaderBundle.GREYSCALE)
            .opacity(Percent.half());

        assertThat(options.opacity()).isEqualTo(Percent.half());
        assertThat(options.drawOrder()).isEqualTo(4);
        assertThat(options.shader()).isEqualTo(ShaderBundle.GREYSCALE.get());
        assertThat(options.scale()).isEqualTo(3.1);
    }
}
