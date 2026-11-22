package dev.screwbox.playground.gamescene.teams.health;

import dev.screwbox.core.Ease;
import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.ui.Notification;
import dev.screwbox.core.ui.NotificationDesign;
import dev.screwbox.core.ui.NotificationDetails;
import dev.screwbox.playground.gamescene.gamemodes.ctf.FlagCarrierComponent;
import dev.screwbox.playground.gamescene.teams.HeavenComponent;
import dev.screwbox.playground.gamescene.teams.PlayerComponent;

public class HealthSystem implements EntitySystem {

    private static final Archetype LIVING = Archetype.of(HealthComponent.class);

    private boolean isUpdated = false;
    @Override
    public void update(Engine engine) {
        //TODO dynamically register team colors
        if(!isUpdated) {
            engine.ui().setNotificationDesign(new NotificationDesign() {

                @Override
                public void render(Notification notification, Canvas canvas) {
                    canvas.drawText(Offset.at(32, canvas.height() / 2), notification.text(), TextDrawOptions.font(FontBundle.SKINNY_SANS)
                        .scale(2)
                        .style(1, FontBundle.SKINNY_SANS.get().replaceColor(Color.WHITE, Color.BLUE), (ShaderSetup) null)
                        .style(2, FontBundle.SKINNY_SANS.get().replaceColor(Color.WHITE, Color.RED), (ShaderSetup) null)
                        .opacity(Ease.IN_PLATEAU.applyOn(notification.progress())));
                }
            });
            isUpdated=true;
        }

        for (final var living : engine.environment().fetchAll(LIVING)) {
            if (living.get(HealthComponent.class).health.isZero()) {
                final var player = living.get(PlayerComponent.class);
                player.deathTime = engine.loop().time();
                living.remove(FlagCarrierComponent.class);
                engine.environment().fetchSingletonComponent(HeavenComponent.class).deadPlayers.add(living);
                engine.environment().remove(living);

                boolean isRed = player.team.equals(Color.RED);

                //TODO by whom?
                engine.ui().showNotification(NotificationDetails.text("%s%s%s has been killed".formatted(
                    isRed ? "{{" : "{",
                    player.name,
                    isRed ? "}}" : "}"
                )));
            }
        }
    }
}
