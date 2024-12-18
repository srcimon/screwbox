package io.github.srcimon.screwbox.core.ui.presets;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.ui.Notification;
import io.github.srcimon.screwbox.core.ui.NotificationRenderer;

/**
 * Renders {@link Notification} with spinning Icon.
 */
public class SpinningIconNotificationRenderer implements NotificationRenderer {

    private static final TextDrawOptions TEXT_OPTIONS = TextDrawOptions.font(FontBundle.BOLDZILLA).scale(1.25);

    @Override
    public void render(final Notification notification, final ScreenBounds bounds, final Canvas canvas) {
        notification.icon().ifPresentOrElse(icon -> {
                    final var iconScale = 24.0 / icon.height();
                    canvas.drawSprite(icon, bounds.offset().addY(4), SpriteDrawOptions.scaled(iconScale).spin(notification.progress()));
                    canvas.drawText(bounds.offset().add(32, bounds.height() / 2), notification.text(), TEXT_OPTIONS.opacity(Ease.IN_PLATEAU.applyOn(notification.progress())));
                },
                () -> canvas.drawText(bounds.offset().addY(bounds.height() / 2), notification.text(), TEXT_OPTIONS.opacity(Ease.IN_PLATEAU.applyOn(notification.progress()))));
    }
}
