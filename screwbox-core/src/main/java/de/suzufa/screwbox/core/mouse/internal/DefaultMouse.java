package de.suzufa.screwbox.core.mouse.internal;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.loop.internal.Updatable;
import de.suzufa.screwbox.core.mouse.Mouse;
import de.suzufa.screwbox.core.mouse.MouseButton;
import de.suzufa.screwbox.core.utils.Latch;
import de.suzufa.screwbox.core.utils.TrippleLatch;

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
    private boolean isCursorOnWindow;
    private Offset lastPosition = Offset.origin();
    private Latch<Integer> unitsScrolled = Latch.of(0, 0);

    public DefaultMouse(final Graphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public Vector drag() {
        final Vector current = worldPosition();
        final Vector last = graphics.worldPositionOf(lastPosition);
        return last.substract(current);
    }

    @Override
    public Offset position() {
        return position;
    }

    @Override
    public Vector worldPosition() {
        return graphics.worldPositionOf(position);
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
        isCursorOnWindow = true;
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        isCursorOnWindow = false;
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

    private void updateMousePosition(final MouseEvent e) {
        final var windowPosition = Offset.at(e.getXOnScreen(), e.getYOnScreen());
        position = windowPosition.substract(graphics.screen().position());
    }

    @Override
    public boolean isCursorOnWindow() {
        return isCursorOnWindow;
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

}
