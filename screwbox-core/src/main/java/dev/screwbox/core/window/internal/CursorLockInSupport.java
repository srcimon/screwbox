package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.mouse.Mouse;

import java.awt.*;

import static java.lang.Math.clamp;

public record CursorLockInSupport(Robot robot, Mouse mouse) {

    public void lockInCursor(final ScreenBounds bounds, final int padding) {
        final int minX = bounds.offset().x() + padding;
        final int maxX = bounds.offset().x() + bounds.width() - padding;

        final int minY = bounds.offset().y() + padding;
        final int maxY = bounds.offset().y() + bounds.height() - padding;

        final Offset targetMouse = Offset.at(
                clamp(mouse.offset().x() + bounds.offset().x(), minX, maxX),
                clamp(mouse.offset().y() + bounds.offset().y(), minY, maxY));

        if (!bounds.offset().add(mouse.offset()).equals(targetMouse)) {
            robot.mouseMove(targetMouse.x(), targetMouse.y());
        }
    }
}
