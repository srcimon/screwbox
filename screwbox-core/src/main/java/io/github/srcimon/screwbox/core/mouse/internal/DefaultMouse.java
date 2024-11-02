package io.github.srcimon.screwbox.core.mouse.internal;

import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultScreen;
import io.github.srcimon.screwbox.core.graphics.internal.ViewportManager;
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

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class DefaultMouse implements Mouse, Updatable, MouseListener, MouseMotionListener, MouseWheelListener {

    private static final Map<Integer, MouseButton> MAPPINGS = Map.of(
            1, MouseButton.LEFT,
            2, MouseButton.MIDDLE,
            3, MouseButton.RIGHT);

    private final Set<MouseButton> downButtons = new HashSet<>();
    private final TrippleLatch<Set<MouseButton>> pressedButtons = TrippleLatch.of(
            new HashSet<>(), new HashSet<>(), new HashSet<>());
    private final DefaultScreen screen;
    private final ViewportManager viewportManager;
    private boolean isCursorOnScreen;
    private Offset lastPosition = Offset.origin();
    private final Latch<Integer> unitsScrolled = Latch.of(0, 0);

    private Offset offset = Offset.origin();
    private Vector position = Vector.zero();
    private Viewport hoverViewport;

    public DefaultMouse(final DefaultScreen screen, final ViewportManager viewportManager) {
        this.screen = screen;
        this.viewportManager = viewportManager;
        hoverViewport = viewportManager.primaryViewport();
    }

    @Override
    public Vector drag() {
        final Vector current = position();
        final Vector last = toPositionConsideringRotation(lastPosition);
        return last.substract(current);
    }

    @Override
    public Offset offset() {
        return offset;
    }

    @Override
    public Vector position() {
        return position;
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
        hoverViewport = calculateHoverViewport();
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
        offset = windowPosition.substract(screen.position()).substract(viewportManager.defaultViewport().canvas().offset());
        hoverViewport = calculateHoverViewport();
        position = toPositionConsideringRotation(offset);
    }

    private Viewport calculateHoverViewport() {
        if (viewportManager.viewports().isEmpty()) {
            return viewportManager.primaryViewport();
        }
        for (final var viewport : viewportManager.viewports()) {
            final Offset fixedOffset = getFixedOffset();
            if (viewport.canvas().bounds().contains(fixedOffset)) {
                return viewport;
            }
        }
        return viewportManager.primaryViewport();
    }

    private Offset getFixedOffset() {
        final Offset target = offset.add(viewportManager.defaultViewport().canvas().offset());
        if(screen.rotation().isNone()) {
            return target;
        }
        final Offset center = viewportManager.defaultViewport().canvas().center();
        return screen.rotation().invert().rotateAroundCenter(center, target);

    }

    private Vector toPositionConsideringRotation(final Offset offset) {
        final Vector mousePosition = screenToWorld(offset);
        if (screen.absoluteRotation().isNone()) {
            return mousePosition;
        }

        final Vector center = screenToWorld(screen.size().center().substract(viewportManager.defaultViewport().canvas().offset()));
        final var delta = Line.between(center, mousePosition);
        return screen.absoluteRotation().invert().applyOn(delta).to();
    }

    private Vector screenToWorld(final Offset offset) {
        final Canvas hoverCanvas = hoverViewport.canvas();
        final Offset fixedOffset = offset.substract(hoverCanvas.offset()).add(viewportManager.defaultViewport().canvas().offset());
        final var camera = hoverViewport.camera();
        final double x = (fixedOffset.x() - (hoverCanvas.width() / 2.0)) / camera.zoom() + camera.focus().x();
        final double y = (fixedOffset.y() - (hoverCanvas.height() / 2.0)) / camera.zoom() + camera.focus().y();

        return Vector.of(x, y);
    }

    @Override
    public Viewport hoverViewport() {
        return hoverViewport;
    }
}
