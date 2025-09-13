package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.mouse.Mouse;

import java.awt.*;

public record MouseLockInSupport(Robot robot, Mouse mouse) {

    public void lockIn(final ScreenBounds bounds, final int padding) {
        int targetX = calcTargetX(bounds, padding);
        int targetY = calcTargetY(bounds, padding);

        Offset currentMouse = bounds.offset().add(mouse.offset());
        Offset targetMouse = Offset.at(targetX, targetY);
        if (!currentMouse.equals(targetMouse)) {
            robot.mouseMove(targetX, targetY);
        }
    }

    private Integer calcTargetX(ScreenBounds bounds, int padding) {
        if (mouse.offset().x() < padding) {
            return bounds.offset().x() + padding;
        } else if (mouse.offset().x() > bounds.width() - padding) {
            return bounds.offset().x() + bounds.width() - padding;
        }
        return mouse.offset().x() + bounds.offset().x();
    }

    private Integer calcTargetY(ScreenBounds bounds, int padding) {
        if (mouse.offset().y() < padding) {
            return bounds.offset().y() + padding;
        } else if (mouse.offset().y() > bounds.height() - padding) {
            return bounds.offset().y() + bounds.height() - padding;
        }
        return mouse.offset().y() + bounds.offset().y();
    }


}
