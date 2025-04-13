package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ShaderBundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpriteFillOptionsTest {

    @Test
    void newInstance_scaleZero_throwsException() {
        assertThatThrownBy(() -> SpriteFillOptions.scale(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("scale must be positive");
    }

    @Test
    void newInstance_opacityNull_throwsException() {
        final var options = SpriteFillOptions.scale(1);
        assertThatThrownBy(() -> options.opacity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("opacity must not be null");
    }

    @Test
    void newInstance_offsetNull_throwsException() {
        final var options = SpriteFillOptions.scale(1);

        assertThatThrownBy(() -> options.offset(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("offset must not be null");
    }

    @Test
    void newInstance_validInputs_returnsInstance() {
        final var options = SpriteFillOptions.scale(1)
                .shaderSetup(ShaderBundle.WATER)
                .opacity(Percent.half())
                .offset(Offset.at(10, 20));

        assertThat(options.shaderSetup()).isEqualTo(ShaderBundle.WATER.get());
        assertThat(options.opacity()).isEqualTo(Percent.half());
        assertThat(options.offset()).isEqualTo(Offset.at(10, 20));
    }
}
