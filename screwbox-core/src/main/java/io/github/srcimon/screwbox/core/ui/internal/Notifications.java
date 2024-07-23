package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import java.util.ArrayList;
import java.util.List;

import static io.github.srcimon.screwbox.core.graphics.SystemTextDrawOptions.systemFont;

public class Notifications implements Updatable {

    private Duration notificationTimeout = Duration.ofSeconds(6);
    private final Screen screen;

    public Notifications(final Screen screen) {
        this.screen = screen;
    }

    @Override
    public void update() {
        final var updateTime = Time.now();
        final var outdatedNotifications = activeNotifications.stream().
                filter(n -> notificationTimeout.addTo(n.time)
                        .isBefore(updateTime)).toList();

        activeNotifications.removeAll(outdatedNotifications);
        int y = screen.height();
        int maxwidth = 100;
        for (final var notification : activeNotifications) {
            var notificationProgress = notificationTimeout.progress(notification.time, updateTime);
            y -= 20;

            screen.drawLine(
                    Offset.at(screen.center().x(), y-2).addX((int)(-maxwidth * notificationProgress.invert().value())),
                    Offset.at(screen.center().x(), y-2).addX((int)(maxwidth * notificationProgress.invert().value())),
                    LineDrawOptions.color(Color.YELLOW).strokeWidth(2));
            screen.drawText(Offset.at(screen.center().x(), y), notification.message, systemFont("Arial")
                    .size(12).bold()
                    .color(Color.WHITE.opacity(notificationProgress.invert()))
                    .alignCenter());
        }
    }

    private record ActiveNotification(String message, Time time) {

    }

    private List<ActiveNotification> activeNotifications = new ArrayList<>();


//TODO More performantly remove items from list

    public void add(final String message) {
        activeNotifications.add(new ActiveNotification(message, Time.now()));
    }

}
