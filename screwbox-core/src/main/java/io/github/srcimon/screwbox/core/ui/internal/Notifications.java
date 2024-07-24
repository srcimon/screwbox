package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.TextDrawOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import java.util.ArrayList;
import java.util.List;

public class Notifications implements Updatable {

    private Duration notificationTimeout = Duration.ofSeconds(4);
    private final Screen screen;

    public Notifications(final Screen screen) {
        this.screen = screen;
    }

    public void add(final String message) {
        activeNotifications.add(new ActiveNotification(message, Time.now()));
    }

    @Override
    public void update() {
        final var updateTime = Time.now();
        final var outdatedNotifications = activeNotifications.stream().
                filter(n -> notificationTimeout.addTo(n.time).isBefore(updateTime))
                .toList();
        activeNotifications.removeAll(outdatedNotifications);
//TODO no title but line breaks
        int y = 10;
        //TODO TextDrawOptions.enableLinefeedAfter(characters)
        for (final var notification : activeNotifications) {
            var notificationProgress = notificationTimeout.progress(notification.time, updateTime);
            Percent val = Ease.IN_PLATEAU_OUT.applyOn(notificationProgress);
            double inFlowX = val.invert().value() * -1000 + 10;
            TextDrawOptions font = TextDrawOptions.font(FontBundle.BOLDZILLA.customColor(Color.WHITE)).scale(1.2);
            TextDrawOptions font2 = TextDrawOptions.font(FontBundle.BOLDZILLA.customColor(Color.WHITE)).scale(0.8);
            var size = font.sizeOf(notification.message).expand(8);
            size = Size.of(600, size.height() + 20);
            screen.drawRectangle(Offset.at(inFlowX - 4, y - 4), size, RectangleDrawOptions.filled(Color.WHITE.opacity(0.1)));
            screen.drawText(Offset.at(inFlowX, y), notification.message, font);
            y += size.height() + 4+14;
        }
    }

    private record ActiveNotification(String message, Time time) {

    }

    private final List<ActiveNotification> activeNotifications = new ArrayList<>();


//TODO More performantly remove items from list

}
