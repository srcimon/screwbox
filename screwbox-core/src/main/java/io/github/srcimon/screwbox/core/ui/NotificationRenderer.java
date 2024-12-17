package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Canvas;

//TODO javadoc
@FunctionalInterface
public interface NotificationRenderer {

    void render(Notification notification, Canvas canvas);
}
