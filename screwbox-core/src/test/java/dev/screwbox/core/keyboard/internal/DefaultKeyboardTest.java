package dev.screwbox.core.keyboard.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.keyboard.DefaultKey;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.keyboard.KeyCombination;
import dev.screwbox.core.navigation.Borders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.awt.event.KeyEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultKeyboardTest {

    @InjectMocks
    DefaultKeyboard keyboard;

    @Mock
    KeyEvent keyEvent;

    @Test
    void recordedText_notRecording_isEmpty() {
        assertThat(keyboard.recordedText()).isEmpty();
    }

    @Test
    void isRecording_recordingStarted_isTrue() {
        keyboard.startRecording();

        assertThat(keyboard.isRecording()).isTrue();
    }

    @Test
    void stopRecording_notRecording_throwsException() {
        assertThatThrownBy(() -> keyboard.stopRecording())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("keyboard is not recording");
    }

    @Test
    void stopRecording_hasRecordedTwoChars_returnsRecordedText() {
        keyboard.startRecording();

        when(keyEvent.getExtendedKeyCode()).thenReturn(65);
        when(keyEvent.getKeyChar()).thenReturn('a');
        keyboard.keyTyped(keyEvent);

        when(keyEvent.getExtendedKeyCode()).thenReturn(65);
        when(keyEvent.getKeyChar()).thenReturn('b');
        keyboard.keyTyped(keyEvent);

        assertThat(keyboard.stopRecording()).isEqualTo("ab");
    }

    @Test
    void startRecording_alreadyStarted_resetsRecording() {
        keyboard.startRecording();

        when(keyEvent.getExtendedKeyCode()).thenReturn(65);
        when(keyEvent.getKeyChar()).thenReturn('a');
        keyboard.keyTyped(keyEvent);

        keyboard.startRecording();

        when(keyEvent.getExtendedKeyCode()).thenReturn(65);
        when(keyEvent.getKeyChar()).thenReturn('b');
        keyboard.keyTyped(keyEvent);

        assertThat(keyboard.recordedText()).contains("b");
    }

    @Test
    void recordedText_typeAbcAndBackspace_isAb() {
        keyboard.startRecording();

        when(keyEvent.getExtendedKeyCode()).thenReturn(65);
        when(keyEvent.getKeyChar()).thenReturn('a');
        keyboard.keyTyped(keyEvent);

        when(keyEvent.getExtendedKeyCode()).thenReturn(66);
        when(keyEvent.getKeyChar()).thenReturn('b');
        keyboard.keyTyped(keyEvent);

        when(keyEvent.getExtendedKeyCode()).thenReturn(67);
        when(keyEvent.getKeyChar()).thenReturn('c');
        keyboard.keyTyped(keyEvent);

        // BACKSPACE
        when(keyEvent.getExtendedKeyCode()).thenReturn(8);
        keyboard.keyTyped(keyEvent);

        assertThat(keyboard.recordedText()).contains("ab");
    }


    @Test
    void isAnyKeyPressed_spaceWasPressedSameFrame_isFalse() {
        mockKeyPress(Key.SPACE);

        assertThat(keyboard.isAnyKeyPressed()).isFalse();
    }

    @Test
    void isAnyKeyPressed_spaceWasPressedNextFrame_isTrue() {
        mockKeyPress(Key.SPACE);
        keyboard.update();

        assertThat(keyboard.isAnyKeyPressed()).isTrue();
    }

    @Test
    void isAnyKeyPressed_noKeyPressed_isFalse() {
        assertThat(keyboard.isAnyKeyPressed()).isFalse();
    }

    @Test
    void isAnyKeyDown_spaceWasPressed_isTrue() {
        mockKeyPress(Key.SPACE);

        assertThat(keyboard.isAnyKeyDown()).isTrue();
    }

    @Test
    void isAnyKeyDown_noKeyPressed_isFalse() {
        assertThat(keyboard.isAnyKeyDown()).isFalse();
    }

    @Test
    void isDown_keyWasPressedAndNotReleased_isTrue() {
        mockKeyPress(Key.SPACE);

        assertThat(keyboard.isDown(Key.SPACE)).isTrue();
    }

    @Test
    void isDown_keyWasPressedAndFocusWasLost_isFalse() {
        mockKeyPress(Key.SPACE);
        keyboard.windowLostFocus(null);

        assertThat(keyboard.isDown(Key.SPACE)).isFalse();
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
    void isDown_allKeysOfCombinationDown_isTrue() {
        mockKeyPress(Key.SPACE);
        mockKeyPress(Key.ARROW_DOWN);
        KeyCombination combination = KeyCombination.ofKeys(Key.SPACE, Key.ARROW_DOWN);

        keyboard.keyPressed(keyEvent);
        keyboard.keyPressed(keyEvent);

        assertThat(keyboard.isDown(combination)).isTrue();
    }

    @Test
    void isPressed_secondCallButUnreleased_false() {
        mockKeyPress(Key.SPACE);

        keyboard.update();
        keyboard.update();

        assertThat(keyboard.isPressed(Key.SPACE)).isFalse();
    }

    @Test
    void isPressed_notReleasedButSecondUpdate_false() {
        mockKeyPress(Key.ENTER);

        keyboard.update();
        keyboard.update();

        assertThat(keyboard.isPressed(Key.ENTER)).isFalse();
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

    @Test
    void getKeyForAlias_noAliasSpecified_isEmpty() {
        assertThat(keyboard.getKeyForAlias(Borders.ALL)).isEmpty();
    }

    enum MyControls {
        @DefaultKey(Key.SPACE)
        JUMP,
        LEFT
    }

    @Test
    void getKeyForAlias_keySpaceDefinedByAnnotation_returnsKeySpace() {
        assertThat(keyboard.getKeyForAlias(MyControls.JUMP)).contains(Key.SPACE);
    }

    @Test
    void bindAlias_keyHasDefaultValue_overwritesDefault() {
        keyboard.bindAlias(MyControls.JUMP, Key.ENTER);

        assertThat(keyboard.getKeyForAlias(MyControls.JUMP)).contains(Key.ENTER);
    }

    @Test
    void bindAlias_aliasNull_throwsException() {
        assertThatThrownBy(() -> keyboard.bindAlias(null, Key.NUMBER_1))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("alias must not be null");
    }

    @Test
    void bindAlias_keyNull_throwsException() {
        assertThatThrownBy(() -> keyboard.bindAlias(MyControls.JUMP, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("key must not be null");
    }

    @Test
    void isDown_noBindingForEnumValue_throwsException() {
        assertThatThrownBy(() -> keyboard.isDown(MyControls.LEFT))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("missing key binding for MyControls.LEFT");
    }

    @Test
    void isDown_hasBindingButIsNotDown_isFalse() {
        assertThat(keyboard.isDown(MyControls.JUMP)).isFalse();
    }

    @Test
    void isDown_aliasNull_throwsException() {
        assertThatThrownBy(() -> keyboard.isDown((MyControls) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("alias must not be null");
    }

    @Test
    void isPressed_hasBindingButIsNotDown_isFalse() {
        assertThat(keyboard.isPressed(MyControls.JUMP)).isFalse();
    }

    @Test
    void isPressed_aliasNull_throwsException() {
        assertThatThrownBy(() -> keyboard.isPressed((MyControls) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("alias must not be null");
    }

    @Test
    void isDown_keyPressedAndReleased_isFalse() {
        mockKeyPress(Key.Y);
        mockKeyRelease(Key.Y);

        assertThat(keyboard.isDown(Key.Y)).isFalse();
    }

    @Test
    void isDown_keyPressedAndOtherReleased_isTrue() {
        mockKeyPress(Key.Y);
        mockKeyRelease(Key.Z);

        assertThat(keyboard.isDown(Key.Y)).isTrue();
    }

    private void mockKeyRelease(Key key) {
        when(keyEvent.getKeyCode()).thenReturn(key.code());
        keyboard.keyReleased(keyEvent);
    }

    private void mockKeyPress(Key key) {
        when(keyEvent.getKeyCode()).thenReturn(key.code());
        keyboard.keyPressed(keyEvent);
    }
}
