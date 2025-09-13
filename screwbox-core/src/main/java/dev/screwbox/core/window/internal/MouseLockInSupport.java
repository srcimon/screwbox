package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.mouse.Mouse;

import java.awt.*;

import static java.util.Objects.nonNull;

public record MouseLockInSupport(Robot robot, Mouse mouse) {

    public void lockIn(final ScreenBounds bounds, final int padding) {
        Integer forceX = null;
        Integer forceY = null;
        if (mouse.offset().x() < padding) {
            forceX = bounds.offset().x() + padding;
        } else if (mouse.offset().x() > bounds.width() - padding) {
            forceX = bounds.offset().x() + bounds.width() - padding;
        }
        if (mouse.offset().y() < padding) {
            forceY = bounds.offset().y() + padding;
        } else if (mouse.offset().y() > bounds.height() - padding) {
            forceY = bounds.offset().y() + bounds.height() - padding;
        }
        if (nonNull(forceX) && nonNull(forceY)) {
            robot.mouseMove(forceX, forceY);
        } else if (nonNull(forceX)) {
            robot.mouseMove(forceX, bounds.offset().y() + mouse.offset().y());
        } else if (nonNull(forceY)) {
            robot.mouseMove(bounds.offset().x() + mouse.offset().x(), forceY);
        }
    }
}
