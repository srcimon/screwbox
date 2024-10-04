package io.github.srcimon.screwbox.core.mouse.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.mouse.Mouse;
import io.github.srcimon.screwbox.core.mouse.MouseButton;
import io.github.srcimon.screwbox.core.utils.Latch;
import io.github.srcimon.screwbox.core.utils.TrippleLatch;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultMouse implements Mouse, Updatable, MouseListener, MouseMotionListener, MouseWheelListener {

    private static final Map<Integer, MouseButton> MAPPINGS = Map.of(
            1, MouseButton.LEFT,
            2, MouseButton.MIDDLE,
            3, MouseButton.RIGHT);

    private final Set<MouseButton> downButtons = new HashSet<>();
    private final TrippleLatch<Set<MouseButton>> pressedButtons = TrippleLatch.of(
            new HashSet<>(), new HashSet<>(), new HashSet<>());
    private final Graphics graphics;
    private Offset offset = Offset.origin();
    private boolean isCursorOnScreen;
    private Offset lastPosition = Offset.origin();
    private final Latch<Integer> unitsScrolled = Latch.of(0, 0);

    public DefaultMouse(final Graphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public Vector drag() {
        final Vector current = position();
        final Vector last = graphics.screenToPosition(lastPosition);
        return last.substract(current);
    }

    @Override
    public Offset offset() {
        return offset;
    }

    @Override
    public Vector position() {
        return graphics.screenToPosition(offset);
    }

    @Override
    public boolean isDown(final MouseButton button) {
        return downButtons.contains(button);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        // not used
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final var mouseButton = MAPPINGS.get(e.getButton());
        downButtons.add(mouseButton);
        pressedButtons.active().add(mouseButton);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        final var mouseButton = MAPPINGS.get(e.getButton());
        downButtons.remove(mouseButton);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        isCursorOnScreen = true;
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        downButtons.clear();
        isCursorOnScreen = false;
    }

    @Override
    public boolean isPressed(final MouseButton button) {
        return pressedButtons.inactive().contains(button);
    }

    @Override
    public void update() {
        unitsScrolled.toggle();
        unitsScrolled.assignActive(0);
        lastPosition = offset;
        pressedButtons.backupInactive().clear();
        pressedButtons.toggle();
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        updateMousePosition(e);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        updateMousePosition(e);
    }

    @Override
    public boolean isCursorOnScreen() {
        return isCursorOnScreen;
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
        unitsScrolled.assignActive(unitsScrolled.active() + e.getUnitsToScroll());
    }

    @Override
    public int unitsScrolled() {
        return unitsScrolled.inactive();
    }

    @Override
    public boolean hasScrolled() {
        // ignores scrolling up and down again with the same values (because it's rather
        // unlikely that this will happen within a fraction of a second)
        return unitsScrolled() != 0;
    }

    @Override
    public boolean isAnyButtonDown() {
        return !downButtons.isEmpty();
    }

    private void updateMousePosition(final MouseEvent e) {
        final var windowPosition = Offset.at(e.getXOnScreen(), e.getYOnScreen());
        offset = windowPosition.substract(graphics.screen().position());
    }
}
