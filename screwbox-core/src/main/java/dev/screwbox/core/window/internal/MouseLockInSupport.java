package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.mouse.Mouse;

import java.awt.*;

import static java.util.Objects.nonNull;

public record MouseLockInSupport(Robot robot, Mouse mouse) {

    public void lockIn(final ScreenBounds bounds, final int padding) {
        Integer targetX = calcTargetX(bounds, padding);
        Integer targetY = calcTargetY(bounds, padding);

        Offset currentMouse = bounds.offset().add(mouse.offset());
        if (nonNull(targetX) && nonNull(targetY)) {
            robot.mouseMove(targetX, targetY);
        } else if (nonNull(targetX)) {
            robot.mouseMove(targetX, currentMouse.y());
        } else if (nonNull(targetY)) {
            robot.mouseMove(currentMouse.x(), targetY);
        }
    }

    private Integer calcTargetX(ScreenBounds bounds, int padding) {
        if (mouse.offset().x() < padding) {
            return bounds.offset().x() + padding;
        } else if (mouse.offset().x() > bounds.width() - padding) {
            return bounds.offset().x() + bounds.width() - padding;
        }
        return null;
    }

    private Integer calcTargetY(ScreenBounds bounds, int padding) {
        if (mouse.offset().y() < padding) {
            return bounds.offset().y() + padding;
        } else if (mouse.offset().y() > bounds.height() - padding) {
            return bounds.offset().y() + bounds.height() - padding;
        }
        return null;
    }


}
