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
    private int unitsScrolled = 0;

    public DefaultMouse(final Graphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public Vector drag() {
        Vector current = worldPosition();
        Vector last = graphics.screenToWorld(lastPosition);
        return last.substract(current);
    }

    @Override
    public Offset position() {
        return position;
    }

    @Override
    public Vector worldPosition() {
        return graphics.screenToWorld(position);
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
        justPressed.primary().add(mouseButton);
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
        return justPressed.backup().contains(button);
    }

    @Override
    public void update() {
        unitsScrolled = 0;
        lastPosition = position;
        justPressed.secondaryBackup().clear();
        justPressed.swap();
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
        position = windowPosition.substract(graphics.window().position());
    }

    @Override
    public boolean isCursorOnWindow() {
        return isCursorOnWindow;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        unitsScrolled = e.getUnitsToScroll();

    }

    @Override
    public int unitsScrolled() {
        return unitsScrolled;
    }

    @Override
    public boolean hasScrolled() {
        return unitsScrolled() != 0;
    }

}
