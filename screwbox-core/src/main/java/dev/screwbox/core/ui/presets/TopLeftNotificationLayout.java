package dev.screwbox.core.ui.presets;

import dev.screwbox.core.Ease;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.ui.Notification;
import dev.screwbox.core.ui.NotificationLayout;


public class TopLeftNotificationLayout implements NotificationLayout {

    private final int height;
    private final int padding;

    public TopLeftNotificationLayout() {
        this(24, 2);
    }

    public TopLeftNotificationLayout(int height, int padding) {
        this.height = height;
        this.padding = padding;
    }

    @Override
    public ScreenBounds layout(final int index, final Notification notification, final ScreenBounds canvasBounds) {
        final var progress = Ease.PLATEAU_OUT.applyOn(notification.progress()).value();
        int width = canvasBounds.width() / 2 - 2 * padding;
        return new ScreenBounds(-width + padding + (int) (progress * width), padding + index * (padding + height), width, height);
    }
}
