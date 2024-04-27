package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.assets.FontBundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextDrawOptionsTest {

    @Test
    void newInstance_fontNull_throwsException() {
        assertThatThrownBy(() -> TextDrawOptions.font((Pixelfont) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("font must not be null");
    }

    @Test
    void newInstance_opacityNull_throwsException() {
        var options = TextDrawOptions.font(new Pixelfont());

        assertThatThrownBy(() -> options.opacity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("opacity must not be null");
    }

    @Test
    void sizeOf_uppercaseText_returnsSizeOfText() {
        var options = TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(2).uppercase();

        assertThat(options.sizeOf("Some kind of lane text")).isEqualTo(Size.of(238, 8));
    }

    @Test
    void widthOf_text_returnsSizeOfText() {
        var options = TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(2);

        assertThat(options.widthOf("Some kind of lane text")).isEqualTo(224);
    }
}
