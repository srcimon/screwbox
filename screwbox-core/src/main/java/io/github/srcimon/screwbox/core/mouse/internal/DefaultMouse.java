package io.github.srcimon.screwbox.core.mouse.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.mouse.Mouse;
import io.github.srcimon.screwbox.core.mouse.MouseButton;
import io.github.srcimon.screwbox.core.utils.Latch;
import io.github.srcimon.screwbox.core.utils.TrippleLatch;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultMouse implements Mouse, Updatable, MouseListener, MouseMotionListener, MouseWheelListener {

    private static final Map<Integer, MouseButton> MAPPINGS = Map.of(
            1, MouseButton.LEFT,
            2, MouseButton.MIDDLE,
            3, MouseButton.RIGHT);

    private final Set<MouseButton> pressed = new HashSet<>();
    private final TrippleLatch<Set<MouseButton>> justPressed = TrippleLatch.of(
            new HashSet<>(), new HashSet<>(), new HashSet<>());
    private final Graphics graphics;
    private Offset position = Offset.origin();
    private boolean isCursorOnScreen;
    private Offset lastPosition = Offset.origin();
    private final Latch<Integer> unitsScrolled = Latch.of(0, 0);

    public DefaultMouse(final Graphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public Vector drag() {
        final Vector current = worldPosition();
        final Vector last = graphics.toPosition(lastPosition);
        return last.substract(current);
    }

    @Override
    public Offset position() {
        return position;
    }

    @Override
    public Vector worldPosition() {
        return graphics.toPosition(position);
    }

    @Override
    public boolean isDown(final MouseButton button) {
        return pressed.contains(button);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        // not used
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final var mouseButton = MAPPINGS.get(e.getButton());
        pressed.add(mouseButton);
        justPressed.active().add(mouseButton);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        final var mouseButton = MAPPINGS.get(e.getButton());
        pressed.remove(mouseButton);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        isCursorOnScreen = true;
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        pressed.clear();
        isCursorOnScreen = false;
    }

    @Override
    public boolean justClicked(final MouseButton button) {
        return justPressed.inactive().contains(button);
    }

    @Override
    public void update() {
        unitsScrolled.toggle();
        unitsScrolled.assignActive(0);
        lastPosition = position;
        justPressed.backupInactive().clear();
        justPressed.toggle();
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
        return !pressed.isEmpty();
    }

    private void updateMousePosition(final MouseEvent e) {
        final var windowPosition = Offset.at(e.getXOnScreen(), e.getYOnScreen());
        position = windowPosition.substract(graphics.screen().position());
    }
}
