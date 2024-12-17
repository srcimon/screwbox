package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;

//TODO javadoc
@FunctionalInterface
public interface NotificationLayouter {

    //TODO comment: must not be outside of original canvas
    ScreenBounds layout(int index, Notification notification, ScreenBounds canvasBounds);
}
