package dev.screwbox.core.graphics.postfilter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BeeEyePostFilterTest {

    @Test
    void newInstance_eyesTooSmall_throwsException() {
        Assertions.assertThatThrownBy(() -> new BeeEyePostFilter(2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("eye size must be in range 8 to 128 (actual value: 2)");
    }
}
