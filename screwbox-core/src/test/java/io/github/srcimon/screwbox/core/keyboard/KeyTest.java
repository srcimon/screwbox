package io.github.srcimon.screwbox.core.keyboard;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KeyTest {

    @Test
    void fromCode_code35_returnsPageDown() {
        assertThat(Key.fromCode(35)).isEqualTo(Key.PAGE_DOWN);
    }

    @Test
    void fromCode_codeNegtive7_returnsPageDown() {
        assertThatThrownBy(() -> Key.fromCode(-7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("key code is invalid: -7");
    }
}
