package dev.screwbox.playground.design;

import dev.screwbox.core.Ease;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.ui.Notification;
import dev.screwbox.core.ui.NotificationDesign;

public class InGameNotificationDesign implements NotificationDesign {

    private static final Asset<TextDrawOptions> OPTIONS = Asset.asset(() -> TextDrawOptions.font(FontBundle.SKINNY_SANS)
        .scale(2)
        .style(1, FontBundle.SKINNY_SANS.get().replaceColor(Color.WHITE, Color.BLUE), (ShaderSetup) null)
        .style(2, FontBundle.SKINNY_SANS.get().replaceColor(Color.WHITE, Color.RED), (ShaderSetup) null)
        .style(3, FontBundle.SKINNY_SANS.get().replaceColor(Color.WHITE, Color.YELLOW), (ShaderSetup) null)
        .style(4, FontBundle.SKINNY_SANS.get().replaceColor(Color.WHITE, Color.GREEN), (ShaderSetup) null)
    );

    @Override
    public void render(Notification notification, Canvas canvas) {
        canvas.drawText(Offset.at(32, canvas.height() / 2), notification.text(), OPTIONS.get().opacity(Ease.IN_PLATEAU.applyOn(notification.progress())));
    }
}
