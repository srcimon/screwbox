package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.mouse.Mouse;
import dev.screwbox.core.utils.MathUtil;

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
        return Math.clamp(mouse.offset().x() + bounds.offset().x(), padding + bounds.offset().x(),  bounds.offset().x() + bounds.width() - padding);
    }

    private Integer calcTargetY(ScreenBounds bounds, int padding) {
        return Math.clamp(mouse.offset().y() + bounds.offset().y(), padding + bounds.offset().y(),  bounds.offset().y() + bounds.height() - padding);
    }


}
