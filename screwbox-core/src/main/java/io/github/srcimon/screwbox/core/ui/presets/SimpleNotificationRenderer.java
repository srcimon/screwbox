package io.github.srcimon.screwbox.core.ui.presets;

import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.ui.Notification;
import io.github.srcimon.screwbox.core.ui.NotificationRenderer;

//TODO javadoc
//TODO test
public class SimpleNotificationRenderer implements NotificationRenderer {

    @Override
    public void render(final Notification notification, final Canvas canvas) {
        canvas.drawText(Offset.at(0, canvas.height() / 2.0), notification.text(), TextDrawOptions.font(FontBundle.BOLDZILLA).scale(2));
        canvas.drawRectangle(Offset.origin(), canvas.size(), RectangleDrawOptions.outline(Color.BLACK.opacity(0.3)).strokeWidth(2));
    }
}
