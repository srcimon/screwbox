package dev.screwbox.playground.design;

import dev.screwbox.core.Ease;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.ui.Notification;
import dev.screwbox.core.ui.NotificationDesign;
import dev.screwbox.playground.GameConfig;

import java.util.List;

public class InGameNotificationDesign implements NotificationDesign {

    private final TextDrawOptions options;

    public InGameNotificationDesign(final List<GameConfig.TeamConfig> teamConfigs) {
        var baseOptions = TextDrawOptions.font(FontBundle.SKINNY_SANS)
            .scale(2);

        for (int index = 0; index < teamConfigs.size(); ++index) {
            final var teamConfig = teamConfigs.get(index);
            final var teamColorFont = FontBundle.SKINNY_SANS.get().replaceColor(Color.WHITE, teamConfig.color());
            baseOptions = baseOptions.style(index + 1, teamColorFont, (ShaderSetup) null);
        }
        System.out.println(baseOptions);
        options = baseOptions;
    }

    @Override
    public void render(Notification notification, Canvas canvas) {
        final var textOptions = options.opacity(Ease.IN_PLATEAU.applyOn(notification.progress()));
        canvas.drawText(Offset.at(32, canvas.height() / 2), notification.text(), textOptions);
    }

    public static String colorByTeam(final String text, int teamIndex) {
        return "{".repeat(teamIndex + 1) + text + "}".repeat(teamIndex + 1);//TODO stringbuilder
    }
}
