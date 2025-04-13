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
    void justClickedLeft_leftClicked_true() {
        when(mouse.isPressed(MouseButton.LEFT)).thenReturn(true);

        assertThat(mouse.isPressedLeft()).isTrue();
    }

    @Test
    void justClickedLeft_notClicked_false() {
        assertThat(mouse.isPressedLeft()).isFalse();
    }

    @Test
    void justClickedRight_rightClicked_true() {
        when(mouse.isPressed(MouseButton.RIGHT)).thenReturn(true);

        assertThat(mouse.isPressedRight()).isTrue();
    }

    @Test
    void justClickedRight_notClicked_false() {
        assertThat(mouse.isPressedRight()).isFalse();
    }
}
