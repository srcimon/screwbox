package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.TextDrawOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import java.util.ArrayList;
import java.util.List;

import static io.github.srcimon.screwbox.core.graphics.SystemTextDrawOptions.systemFont;

public class Notifications implements Updatable {

    private Duration notificationTimeout = Duration.ofSeconds(4);
    private final Screen screen;

    public Notifications(final Screen screen) {
        this.screen = screen;
    }

    @Override
    public void update() {
        final var updateTime = Time.now();
        final var outdatedNotifications = activeNotifications.stream().
                filter(n -> notificationTimeout.addTo(n.time)
                        .isBefore(updateTime))
                .toList();

        activeNotifications.removeAll(outdatedNotifications);
        int y = 20;
        int maxwidth = 100;
        for (final var notification : activeNotifications) {
            var notificationProgress = notificationTimeout.progress(notification.time, updateTime);
            y += 20;


            Percent val = Ease.IN_PLATEAU_OUT.applyOn(notificationProgress);
            double inFlowX = val.invert().value() * -500;
            screen.drawLine(
                    Offset.at(inFlowX, y-2),
                    Offset.at(inFlowX, y-2).addX((int)(maxwidth * (notificationProgress.invert().value() -0.2) )),
                    LineDrawOptions.color(Color.YELLOW).strokeWidth(2));
            TextDrawOptions font = TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(1.4);
//            screen.drawRectangle(Offset.at(inFlowX, y), font.sizeOf(notification.message), RectangleDrawOptions.filled(Color.WHITE));
            screen.drawText(Offset.at(inFlowX, y), notification.message, font);
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
