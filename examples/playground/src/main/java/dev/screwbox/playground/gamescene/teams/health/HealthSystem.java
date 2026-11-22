package dev.screwbox.playground.gamescene.teams.health;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.ui.NotificationDetails;
import dev.screwbox.core.utils.Cache;
import dev.screwbox.playground.gamescene.gamemodes.ctf.FlagCarrierComponent;
import dev.screwbox.playground.gamescene.teams.HeavenComponent;
import dev.screwbox.playground.gamescene.teams.PlayerComponent;

public class HealthSystem implements EntitySystem {

    private static final Archetype LIVING = Archetype.of(HealthComponent.class);
    private Cache<Color, Sprite> ICONS = new Cache<>();

    @Override
    public void update(Engine engine) {
        for (final var living : engine.environment().fetchAll(LIVING)) {
            if (living.get(HealthComponent.class).health.isZero()) {
                final var player = living.get(PlayerComponent.class);
                player.deathTime = engine.loop().time();
                living.remove(FlagCarrierComponent.class);
                engine.environment().fetchSingletonComponent(HeavenComponent.class).deadPlayers.add(living);
                engine.environment().remove(living);
                engine.ui().showNotification(NotificationDetails.text("{%s} has been killed".formatted(player.name)));
            }
        }
    }
}
