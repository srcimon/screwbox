package io.github.srcimon.screwbox.core.keyboard;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KeyTest {

    @Test
    void fromCode_code35_returnsPageDown() {
        assertThat(Key.fromCode(35)).contains(Key.PAGE_DOWN);
    }

    @Test
    void fromCode_codeNegtive7_isEmpty() {
        assertThat(Key.fromCode(-7)).isEmpty();
    }
}
