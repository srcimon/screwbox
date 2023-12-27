package io.github.srcimon.screwbox.core.keyboard.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.keyboard.KeyCombination;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.event.KeyEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultKeyboardTest {

    @InjectMocks
    DefaultKeyboard keyboard;

    @Mock
    private KeyEvent keyEvent;

    @Test
    void isDown_keyWasPressedAndNotReleased_returnsTrue() {
        mockKeyPress(Key.SPACE);

        assertThat(keyboard.isDown(Key.SPACE)).isTrue();
    }

    @Test
    void isDown_keyWasPressedAndReleased_returnsFalse() {
        mockKeyPress(Key.SPACE);
        keyboard.keyReleased(keyEvent);

        assertThat(keyboard.isDown(Key.SPACE)).isFalse();
    }

    @Test
    void isDown_onlyOneKeyOfCombinationDown_returnsFalse() {
        mockKeyPress(Key.SPACE);

        KeyCombination combination = KeyCombination.ofKeys(Key.SPACE, Key.ARROW_DOWN);

        keyboard.keyPressed(keyEvent);

        assertThat(keyboard.isDown(combination)).isFalse();
    }

    @Test
    void isDown_allKeysOfCombinationDown_returnsTrue() {
        mockKeyPress(Key.SPACE);
        mockKeyPress(Key.ARROW_DOWN);
        KeyCombination combination = KeyCombination.ofKeys(Key.SPACE, Key.ARROW_DOWN);

        keyboard.keyPressed(keyEvent);
        keyboard.keyPressed(keyEvent);

        assertThat(keyboard.isDown(combination)).isTrue();
    }

    @Test
    void isPressed_sameFrame_false() {
        mockKeyPress(Key.SPACE);

        assertThat(keyboard.isPressed(Key.SPACE)).isFalse();
    }

    @Test
    void isPressed_nextFrame_true() {
        mockKeyPress(Key.SPACE);

        keyboard.update();

        assertThat(keyboard.isPressed(Key.SPACE)).isTrue();
    }

    @Test
    void pressedKeys_cAndSpacePressed_returnsCAndSpace() {
        mockKeyPress(Key.A);
        mockKeyPress(Key.SPACE);

        keyboard.update();

        assertThat(keyboard.pressedKeys()).containsExactlyInAnyOrder(Key.A, Key.SPACE);
    }

    @ParameterizedTest
    @CsvSource({
            "W, 0, -16",
            "WS, 0, 0",
            "AD, 0, 0",
            "AS, -11.3, 11.3",
            "XZ, 0, 0",
            "D, 16, 0"
    })
    void wsadMovement_keysPressed_returnsMovement(String pressedKeys, double expectedX, double expectedY) {
        for (char character : pressedKeys.toCharArray()) {
            Key key = Key.valueOf(String.valueOf(character));
            mockKeyPress(key);
        }

        keyboard.update();

        Vector result = keyboard.wsadMovement(16);

        assertThat(result.x()).isEqualTo(expectedX, offset(0.1));
        assertThat(result.y()).isEqualTo(expectedY, offset(0.1));
    }

    @ParameterizedTest
    @CsvSource({
            "ARROW_UP, 0, -40",
            "ARROW_DOWN, 0, 40",
            "ARROW_LEFT, -40, 0",
            "ARROW_RIGHT, 40, 0",
    })
    void arrowKeyMovement_keysPressed_returnsMovement(Key pressedKey, double expectedX, double expectedY) {
        mockKeyPress(pressedKey);

        keyboard.update();

        Vector result = keyboard.arrowKeysMovement(40);

        assertThat(result.x()).isEqualTo(expectedX);
        assertThat(result.y()).isEqualTo(expectedY);
    }

    private void mockKeyPress(Key key) {
        when(keyEvent.getKeyCode()).thenReturn(key.code());
        keyboard.keyPressed(keyEvent);
    }
}
