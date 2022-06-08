package de.suzufa.screwbox.core.keyboard.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.core.keyboard.KeyCombination;
import de.suzufa.screwbox.core.loop.Metrics;

@ExtendWith(MockitoExtension.class)
class DefaultKeyboardTest {

    @InjectMocks
    DefaultKeyboard keyboard;

    @Mock
    private KeyEvent keyEvent;

    @Mock
    private Metrics metrics;

    @Test
    void isDown_keyWasPressedAndNotReleased_returnsTrue() {
        when(keyEvent.getKeyCode()).thenReturn(32);

        keyboard.keyPressed(keyEvent);

        assertThat(keyboard.isDown(Key.SPACE)).isTrue();
    }

    @Test
    void isDown_keyWasPressedAndReleased_returnsFalse() {
        when(keyEvent.getKeyCode()).thenReturn(32);

        keyboard.keyPressed(keyEvent);
        keyboard.keyReleased(keyEvent);

        assertThat(keyboard.isDown(Key.SPACE)).isFalse();
    }

    @Test
    void isDown_onlyOneKeyOfCombinationDown_returnsFalse() {
        when(keyEvent.getKeyCode()).thenReturn(32);
        KeyCombination combination = KeyCombination.ofKeys(Key.SPACE, Key.ARROW_DOWN);

        keyboard.keyPressed(keyEvent);

        assertThat(keyboard.isDown(combination)).isFalse();
    }

    @Test
    void isDown_allKeysOfCombinationDown_returnsTrue() {
        when(keyEvent.getKeyCode()).thenReturn(32, 40);
        KeyCombination combination = KeyCombination.ofKeys(Key.SPACE, Key.ARROW_DOWN);

        keyboard.keyPressed(keyEvent);
        keyboard.keyPressed(keyEvent);

        assertThat(keyboard.isDown(combination)).isTrue();
    }

    @Test
    void justPressed_sameFrame_false() {
        when(keyEvent.getKeyCode()).thenReturn(32);

        keyboard.keyPressed(keyEvent);

        assertThat(keyboard.justPressed(Key.SPACE)).isFalse();
    }

    @Test
    void justPressed_nextFrame_true() {
        when(keyEvent.getKeyCode()).thenReturn(32);

        keyboard.keyPressed(keyEvent);

        keyboard.update();

        assertThat(keyboard.justPressed(Key.SPACE)).isTrue();
    }

}
