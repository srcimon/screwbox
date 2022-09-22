package de.suzufa.screwbox.core.mouse.internal;

import static de.suzufa.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.mouse.MouseButton;

@ExtendWith(MockitoExtension.class)
class DefaultMouseTest {

    @Mock
    Window window;

    @Mock
    Graphics graphics;

    @InjectMocks
    DefaultMouse mouse;

    @Test
    void justClicked_mouseNotPressed_isFalse() {
        var isPressed = mouse.justClicked(MouseButton.LEFT);

        mouse.update();

        assertThat(isPressed).isFalse();
    }

    @Test
    void jjustClicked_mousePressed_isTrue() {
        mouse.mousePressed(rightMouseButtonEvent());
        mouse.update();

        var isPressed = mouse.justClicked(MouseButton.RIGHT);

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
    void isCursorOnWindow_enteredAndExited_isFalse() {
        mouse.mouseEntered(null);
        mouse.mouseExited(null);

        assertThat(mouse.isCursorOnWindow()).isFalse();
    }

    @Test
    void isCursorOnWindow_entered_isTrue() {
        mouse.mouseEntered(null);

        assertThat(mouse.isCursorOnWindow()).isTrue();
    }

    @Test
    void position_returnsTheLatestPosition() {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getXOnScreen()).thenReturn(151);
        when(mouseEvent.getYOnScreen()).thenReturn(242);
        when(graphics.window()).thenReturn(window);
        when(window.position()).thenReturn(Offset.at(40, 12));

        mouse.mouseMoved(mouseEvent);

        assertThat(mouse.position()).isEqualTo(Offset.at(111, 230));
    }

    @Test
    void worldPosition_returnsTheLatestWorldPosition() {
        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getXOnScreen()).thenReturn(151, 219);
        when(mouseEvent.getYOnScreen()).thenReturn(242, 20);
        when(graphics.window()).thenReturn(window);
        when(window.position()).thenReturn(Offset.at(40, 12));
        when(graphics.screenToWorld(Offset.at(111, 230))).thenReturn($(40, 90));
        when(graphics.screenToWorld(Offset.at(179, 8))).thenReturn($(10, 30));
        mouse.mouseMoved(mouseEvent);

        assertThat(mouse.worldPosition()).isEqualTo($(40, 90));

        mouse.mouseDragged(mouseEvent);

        assertThat(mouse.worldPosition()).isEqualTo($(10, 30));
    }

    @Test
    void drag_noMovement_isZero() {
        when(graphics.screenToWorld(Offset.origin())).thenReturn($(40, 90));

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

    private MouseEvent rightMouseButtonEvent() {
        MouseEvent event = mock(MouseEvent.class);
        when(event.getButton()).thenReturn(3);
        return event;
    }
}
