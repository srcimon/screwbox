package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JSonLoaderTest {

    @Test
    void load_jsonNull_throwsException() {
        assertThatThrownBy(() -> JSonLoader.load(null, String.class))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("json must not be null");
    }

    @Test
    void load_typeNull_throwsException() {
        assertThatThrownBy(() -> JSonLoader.load("", null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("type must not be null");
    }
}
