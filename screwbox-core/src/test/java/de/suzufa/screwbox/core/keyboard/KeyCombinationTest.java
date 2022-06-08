package de.suzufa.screwbox.core.keyboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class KeyCombinationTest {

    @Test
    void ofKeys_keysAreSet_returnsKombinationOfKeys() {
        var jumpDown = KeyCombination.ofKeys(Key.ARROW_DOWN, Key.SPACE);

        assertThat(jumpDown.keys()).contains(Key.ARROW_DOWN, Key.SPACE);
    }

    @Test
    void ofKeys_keysIsNull_throwsException() {
        assertThatThrownBy(() -> KeyCombination.ofKeys())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("combination must contain a key");
    }
}
