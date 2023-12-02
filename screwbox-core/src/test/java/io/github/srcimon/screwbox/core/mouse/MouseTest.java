package io.github.srcimon.screwbox.core.mouse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
