package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;

import java.util.ArrayList;
import java.util.List;

import static io.github.srcimon.screwbox.core.graphics.SystemTextDrawOptions.systemFont;

class Notifications {

    private Duration notificationTimeout = Duration.ofSeconds(2);

    private record ActiveNotification(String message, Time time) {

    }

    private List<ActiveNotification> activeNotifications = new ArrayList<>();


//TODO More performantly remove items from list

    public void add(final String message) {
        final Time timeout = notificationTimeout.addTo(Time.now());
        activeNotifications.add(new ActiveNotification(message, timeout));
    }

    public void render(final Screen screen) {
        //TODO removing notifications should not happen in render method
        final var updateTime = Time.now();
        final var outdatedNotifications = activeNotifications.stream().
                filter(n -> notificationTimeout.addTo(n.time)
                        .isBefore(updateTime)).toList();

        activeNotifications.removeAll(outdatedNotifications);
        int y = screen.height();
        for (final var notification : activeNotifications) {
            var notificationProgress = notificationTimeout.progress(notification.time, updateTime);
            y -= 10;
            screen.drawText(Offset.at(screen.center().x(), y), notification.message, systemFont("Arial").color(Color.WHITE.opacity(notificationProgress.invert())).alignCenter());
        }
    }
}
