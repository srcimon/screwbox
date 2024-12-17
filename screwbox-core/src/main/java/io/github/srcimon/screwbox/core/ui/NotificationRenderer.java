package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;

//TODO javadoc
@FunctionalInterface
public interface NotificationRenderer {

    void render(Notification notification, ScreenBounds bounds, Canvas canvas);
}
