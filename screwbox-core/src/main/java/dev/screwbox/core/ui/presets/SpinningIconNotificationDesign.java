package dev.screwbox.core.ui.presets;

import dev.screwbox.core.Ease;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.ui.Notification;
import dev.screwbox.core.ui.NotificationDesign;

/**
 * Renders {@link Notification} with spinning Icon.
 */
public class SpinningIconNotificationDesign implements NotificationDesign {

    private static final TextDrawOptions TEXT_OPTIONS = TextDrawOptions.font(FontBundle.BOLDZILLA).scale(1.25);

    @Override
    public void render(final Notification notification, final Canvas canvas) {
        notification.icon().ifPresentOrElse(icon -> {
                final var iconScale = 24.0 / icon.height();
                canvas.drawSprite(icon, Offset.at(0, 4), SpriteDrawOptions.scaled(iconScale).spin(notification.progress()));
                canvas.drawText(Offset.at(32, canvas.height() / 2), notification.text(), TEXT_OPTIONS.opacity(Ease.IN_PLATEAU.applyOn(notification.progress())));
            },
            () -> canvas.drawText(Offset.at(32, canvas.height() / 2), notification.text(), TEXT_OPTIONS.opacity(Ease.IN_PLATEAU.applyOn(notification.progress()))));
    }
}
