package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextDrawOptionsTest {

    @Test
    void newInstance_fontSizeTooSmall_throwsException() {
        assertThatThrownBy(() -> TextDrawOptions.systemFont("Arial", 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("font size must be at least 4");
    }

    @Test
    void newInstance_fontNameNull_throwsException() {
        assertThatThrownBy(() -> TextDrawOptions.systemFont(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("font name must not be null");
    }

    @Test
    void newInstance_colorNull_throwsException() {
        var options = TextDrawOptions.systemFont("Arial");

        assertThatThrownBy(() -> options.color(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("color must not be null");
    }
}
