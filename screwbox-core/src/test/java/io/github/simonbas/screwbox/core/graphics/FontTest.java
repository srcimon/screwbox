package io.github.simonbas.screwbox.core.graphics;

import io.github.simonbas.screwbox.core.graphics.Font.Style;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FontTest {

    @Test
    void newInstance_noSize_throwsException() {
        assertThatThrownBy(() -> new Font("Futura", 0, Style.NORMAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("font must have size greater 0");
    }

    @Test
    void newInstance_noStyle_throwsException() {
        assertThatThrownBy(() -> new Font("Futura", 4, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("font style must not be null");
    }

    @Test
    void newInstance_noName_throwsException() {
        assertThatThrownBy(() -> new Font(null, 4))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("font name must not be null");
    }

    @Test
    void withSize_returnsSameFontWithDifferentSize() {
        Font myFont = new Font("Arial", 12, Style.ITALIC);
        Font headding = myFont.withSize(30);

        assertThat(headding.name()).isEqualTo("Arial");
        assertThat(headding.style()).isEqualTo(Style.ITALIC);
        assertThat(headding.size()).isEqualTo(30);
    }
}
