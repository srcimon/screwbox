package dev.screwbox.core.graphics.smoke;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.smoke.styles.FireSmokeStyle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SmokeOptionsTest {

    @Test
    void newInstance_allFieldsCustomized_hasCorrectValues() {
        var options = SmokeOptions.noFade()
            .velocity(Vector.y(-2))
            .velocityAdaption(Percent.half())
            .diffusion(Percent.of(0.00004))
            .viscosity(Percent.of(0.001))
            .fade(0.01)
            .iterations(3)
            .style(new FireSmokeStyle());


        assertThat(options.velocity()).isEqualTo(Vector.y(-2));
        assertThat(options.velocityAdaption()).isEqualTo(Percent.half());
        assertThat(options.diffusion()).isEqualTo(Percent.of(0.00004));
        assertThat(options.viscosity()).isEqualTo(Percent.of(0.001));
        assertThat(options.iterations()).isEqualTo(3);
        assertThat(options.fade()).isEqualTo(0.01);
        assertThat(options.style()).isInstanceOf(FireSmokeStyle.class);
    }

    @Test
    void viscosity_null_throwsException() {
        var options = SmokeOptions.noFade();

        assertThatThrownBy(() -> options.viscosity(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("viscosity must not be null");
    }

    @Test
    void diffusion_null_throwsException() {
        var options = SmokeOptions.noFade();

        assertThatThrownBy(() -> options.diffusion(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("diffusion must not be null");
    }

    @Test
    void fade_outOfRange_throwsException() {
        var options = SmokeOptions.noFade();

        assertThatThrownBy(() -> options.fade(-0.01))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("fade must be between 0 and 10 (actual value: -0.01)");
    }

    @Test
    void opacity_null_throwsException() {
        var options = SmokeOptions.noFade();

        assertThatThrownBy(() -> options.opacity(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("opacity must not be null");
    }

    @Test
    void iteration_outOfRange_throwsException() {
        var options = SmokeOptions.noFade();

        assertThatThrownBy(() -> options.iterations(40))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("iterations must be between 0 and 10 (actual value: 40)");
    }

    @Test
    void velocityAdaption_null_throwsException() {
        var options = SmokeOptions.noFade();

        assertThatThrownBy(() -> options.velocityAdaption(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("velocity adaption must not be null");
    }


    @Test
    void style_null_throwsException() {
        var options = SmokeOptions.noFade();

        assertThatThrownBy(() -> options.style(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("style must not be null");
    }
}
