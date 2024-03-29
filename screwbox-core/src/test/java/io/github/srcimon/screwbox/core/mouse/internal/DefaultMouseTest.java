package io.github.srcimon.screwbox.core.mouse.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.mouse.MouseButton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultMouseTest {

    @Mock
    Screen screen;

    @Mock
    Graphics graphics;

    @InjectMocks
    DefaultMouse mouse;

    @Test
    void isPressed_mouseNotPressed_isFalse() {
        var isPressed = mouse.isPressed(MouseButton.LEFT);

        mouse.update();

        assertThat(isPressed).isFalse();
    }

    @Test
    void isPressed_mousePressed_isTrue() {
        mouse.mousePressed(rightMouseButtonEvent());
        mouse.update();

        var isPressed = mouse.isPressed(MouseButton.RIGHT);

        assertThat(isPressed).isTrue();
    }

    @Test
    void isDown_pressedButAlreadyReleased_isFalse() {
        mouse.mousePressed(rightMouseButtonEvent());
        mouse.mouseReleased(rightMouseButtonEvent());

        var isButtonDown = mouse.isDown(MouseButton.RIGHT);

        assertThat(isButtonDown).isFalse();
    }

    @Test
    void isCursorOnScreen_enteredAndExited_isFalse() {
        mouse.mouseEntered(null);
        mouse.mouseExited(null);

        assertThat(mouse.isCursorOnScreen()).isFalse();
    }

    @Test
    void isCursorOnScreen_entered_isTrue() {
        mouse.mouseEntered(null);

        assertThat(mouse.isCursorOnScreen()).isTrue();
    }

    @Test
    void offset_returnsTheLatestOffset() {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getXOnScreen()).thenReturn(151);
        when(mouseEvent.getYOnScreen()).thenReturn(242);
        when(graphics.screen()).thenReturn(screen);
        when(screen.position()).thenReturn(Offset.at(40, 12));

        mouse.mouseMoved(mouseEvent);

        assertThat(mouse.offset()).isEqualTo(Offset.at(111, 230));
    }

    @Test
    void position_returnsTheLatestPosition() {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getXOnScreen()).thenReturn(151, 219);
        when(mouseEvent.getYOnScreen()).thenReturn(242, 20);
        when(graphics.screen()).thenReturn(screen);
        when(screen.position()).thenReturn(Offset.at(40, 12));
        when(graphics.toPosition(Offset.at(111, 230))).thenReturn(Vector.$(40, 90));
        when(graphics.toPosition(Offset.at(179, 8))).thenReturn(Vector.$(10, 30));
        mouse.mouseMoved(mouseEvent);

        assertThat(mouse.position()).isEqualTo(Vector.$(40, 90));

        mouse.mouseDragged(mouseEvent);

        assertThat(mouse.position()).isEqualTo(Vector.$(10, 30));
    }

    @Test
    void drag_noMovement_isZero() {
        when(graphics.toPosition(Offset.origin())).thenReturn(Vector.$(40, 90));

        assertThat(mouse.drag()).isEqualTo(Vector.zero());
    }

    @Test
    void unitsScrolled_noScrolling_isZero() {
        assertThat(mouse.unitsScrolled()).isZero();
    }

    @Test
    void unitsScrolled_scrollupAndDown_returnsSum() {
        MouseWheelEvent scrollUpEvent = mock(MouseWheelEvent.class);
        when(scrollUpEvent.getUnitsToScroll()).thenReturn(4);
        mouse.mouseWheelMoved(scrollUpEvent);

        MouseWheelEvent scrollDownEvent = mock(MouseWheelEvent.class);
        when(scrollDownEvent.getUnitsToScroll()).thenReturn(-7);
        mouse.mouseWheelMoved(scrollDownEvent);

        mouse.update();

        MouseWheelEvent ignoredScrolling = mock(MouseWheelEvent.class);
        when(ignoredScrolling.getUnitsToScroll()).thenReturn(40);
        mouse.mouseWheelMoved(ignoredScrolling);

        assertThat(mouse.unitsScrolled()).isEqualTo(-3);
    }

    @Test
    void hasScrolled_scrollupAndDown_true() {
        MouseWheelEvent scrollUpEvent = mock(MouseWheelEvent.class);
        when(scrollUpEvent.getUnitsToScroll()).thenReturn(4);
        mouse.mouseWheelMoved(scrollUpEvent);

        MouseWheelEvent scrollDownEvent = mock(MouseWheelEvent.class);
        when(scrollDownEvent.getUnitsToScroll()).thenReturn(-7);
        mouse.mouseWheelMoved(scrollDownEvent);

        mouse.update();

        assertThat(mouse.hasScrolled()).isTrue();
    }

    @Test
    void hasScrolled_snotScrolled_false() {
        mouse.update();

        assertThat(mouse.hasScrolled()).isFalse();
    }

    @Test
    void isAnyButtonDown_noButtonDown_false() {
        mouse.update();

        assertThat(mouse.isAnyButtonDown()).isFalse();
    }

    @Test
    void isAnyButtonDown_leftClicked_true() {
        mouse.mousePressed(rightMouseButtonEvent());

        mouse.update();

        assertThat(mouse.isAnyButtonDown()).isTrue();
    }

    private MouseEvent rightMouseButtonEvent() {
        MouseEvent event = mock(MouseEvent.class);
        when(event.getButton()).thenReturn(3);
        return event;
    }
}
