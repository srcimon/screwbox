package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

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

}
