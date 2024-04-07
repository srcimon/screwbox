package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SystemTextDrawOptionsTest {

    @Test
    void newInstance_fontSizeTooSmall_throwsException() {
        assertThatThrownBy(() -> SystemTextDrawOptions.systemFont("Arial", 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("font size must be at least 4");
    }

    @Test
    void newInstance_fontNameNull_throwsException() {
        assertThatThrownBy(() -> SystemTextDrawOptions.systemFont(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("font name must not be null");
    }

    @Test
    void newInstance_colorNull_throwsException() {
        var options = SystemTextDrawOptions.systemFont("Arial");

        assertThatThrownBy(() -> options.color(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("color must not be null");
    }

    @Test
    void newInstance_validOptions_createsNewInstance() {
        var options = SystemTextDrawOptions.systemFont("Arial")
                .bold()
                .color(Color.RED)
                .alignCenter()
                .size(20);

        assertThat(options.fontName()).isEqualTo("Arial");
        assertThat(options.isBold()).isTrue();
        assertThat(options.isItalic()).isFalse();
        assertThat(options.size()).isEqualTo(20);
        assertThat(options.color()).isEqualTo(Color.RED);
    }
}
