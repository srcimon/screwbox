package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.mouse.Mouse;

import java.awt.*;

import static java.lang.Math.clamp;

public record CursorLockInSupport(Robot robot, Mouse mouse) {

    public void lockInCursor(final ScreenBounds bounds, final int padding) {
        final int minX = bounds.x() + padding;
        final int maxX = bounds.x() + bounds.width() - padding;

        final int minY = bounds.y() + padding;
        final int maxY = bounds.y() + bounds.height() - padding;

        final Offset targetMouse = Offset.at(
                clamp((long)mouse.offset().x() + bounds.x(), minX, maxX),
                clamp((long)mouse.offset().y() + bounds.y(), minY, maxY));

        if (!bounds.offset().add(mouse.offset()).equals(targetMouse)) {
            robot.mouseMove(targetMouse.x(), targetMouse.y());
        }
    }
}
