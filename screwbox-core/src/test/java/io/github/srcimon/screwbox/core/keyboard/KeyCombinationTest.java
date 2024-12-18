package io.github.srcimon.screwbox.core.keyboard;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KeyCombinationTest {

    @Test
    void ofKeys_keysAreSet_returnsKombinationOfKeys() {
        var jumpDown = KeyCombination.ofKeys(Key.ARROW_DOWN, Key.SPACE);

        assertThat(jumpDown.keys()).contains(Key.ARROW_DOWN, Key.SPACE);
    }

    @Test
    void ofKeys_noKeys_throwsException() {
        assertThatThrownBy(KeyCombination::ofKeys)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("combination must contain a key");
    }

    @Test
    void ofKeys_duplicateKeys_throwsException() {
        assertThatThrownBy(() -> KeyCombination.ofKeys(Key.SPACE, Key.SPACE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("combination must not contain duplicate keys");
    }
}
