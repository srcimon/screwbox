package io.github.simonbas.screwbox.core.mouse;

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
        when(mouse.justClicked(MouseButton.LEFT)).thenReturn(true);

        assertThat(mouse.justClickedLeft()).isTrue();
    }

    @Test
    void justClickedLeft_notClicked_false() {
        assertThat(mouse.justClickedLeft()).isFalse();
    }

    @Test
    void justClickedRight_rightClicked_true() {
        when(mouse.justClicked(MouseButton.RIGHT)).thenReturn(true);

        assertThat(mouse.justClickedRight()).isTrue();
    }

    @Test
    void justClickedRight_notClicked_false() {
        assertThat(mouse.justClickedRight()).isFalse();
    }
}
