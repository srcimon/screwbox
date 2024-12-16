package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Canvas;

//TODO javadoc
public interface NotificationRenderer {

    void render(NotificationDetails notificationDetails, Percent progress, Canvas canvas);
}
