package dev.screwbox.playground.gamescene.teams.health;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.ui.NotificationDetails;
import dev.screwbox.playground.gamescene.gamemodes.ctf.FlagCarrierComponent;
import dev.screwbox.playground.gamescene.teams.HeavenComponent;
import dev.screwbox.playground.gamescene.teams.PlayerComponent;

import java.util.Map;

public class HealthSystem implements EntitySystem {

    private static final Archetype LIVING = Archetype.of(HealthComponent.class);

    private Map<Color, String> PREFIX = Map.of(
        Color.BLUE, "{",
        Color.RED, "{{",
        Color.YELLOW, "{{{",
        Color.GREEN, "{{{{");

    private Map<Color, String> POSTFIX = Map.of(
        Color.BLUE, "}",
        Color.RED, "}}",
        Color.YELLOW, "}}}",
        Color.GREEN, "}}}}");

    @Override
    public void update(Engine engine) {
        for (final var living : engine.environment().fetchAll(LIVING)) {
            if (living.get(HealthComponent.class).health.isZero()) {
                final var player = living.get(PlayerComponent.class);
                player.deathTime = engine.loop().time();
                living.remove(FlagCarrierComponent.class);
                engine.environment().fetchSingletonComponent(HeavenComponent.class).deadPlayers.add(living);
                engine.environment().remove(living);

                engine.ui().showNotification(NotificationDetails.text("%s%s%s has been killed".formatted(
                    PREFIX.get(player.team), player.name, POSTFIX.get(player.team))));
            }
        }
    }
}
