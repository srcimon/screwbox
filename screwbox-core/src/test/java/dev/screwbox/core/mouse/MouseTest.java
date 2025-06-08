package dev.screwbox.core.mouse;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
class MouseTest {

    @Spy
    Mouse mouse;

    @Test
    void isPressedLeft_leftClicked_true() {
        when(mouse.isPressed(MouseButton.LEFT)).thenReturn(true);

        assertThat(mouse.isPressedLeft()).isTrue();
    }

    @Test
    void isPressedLeft_notClicked_false() {
        assertThat(mouse.isPressedLeft()).isFalse();
    }

    @Test
    void isPressedRight_rightClicked_true() {
        when(mouse.isPressed(MouseButton.RIGHT)).thenReturn(true);

        assertThat(mouse.isPressedRight()).isTrue();
    }

    @Test
    void isPressedRight_rightClicked_true_notClicked_false() {
        assertThat(mouse.isPressedRight()).isFalse();
    }

    @Test
    void isDownLeft_leftClicked_true() {
        when(mouse.isDown(MouseButton.LEFT)).thenReturn(true);

        assertThat(mouse.isDownLeft()).isTrue();
    }

    @Test
    void isDownLeft_notClicked_false() {
        assertThat(mouse.isDownLeft()).isFalse();
    }

    @Test
    void isDownRight_rightClicked_true() {
        when(mouse.isDown(MouseButton.RIGHT)).thenReturn(true);

        assertThat(mouse.isDownRight()).isTrue();
    }

    @Test
    void isDownRight_notClicked_false() {
        assertThat(mouse.isDownRight()).isFalse();
    }
}
