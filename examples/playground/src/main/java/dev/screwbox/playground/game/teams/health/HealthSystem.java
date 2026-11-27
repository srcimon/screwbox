package dev.screwbox.playground.game.teams.health;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.ui.NotificationDetails;
import dev.screwbox.playground.design.InGameNotificationDesign;
import dev.screwbox.playground.game.gamemodes.ctf.FlagCarrierComponent;
import dev.screwbox.playground.game.teams.HeavenComponent;
import dev.screwbox.playground.game.teams.PlayerComponent;
import dev.screwbox.playground.game.teams.TeamInfoComponent;

public class HealthSystem implements EntitySystem {

    private static final Archetype LIVING = Archetype.of(HealthComponent.class, PlayerComponent.class);

    @Override
    public void update(Engine engine) {
        final Environment environment = engine.environment();

        for (final var beeing : environment.fetchAll(LIVING)) {
            if (beeing.get(HealthComponent.class).health.isZero()) {
                final var player = beeing.get(PlayerComponent.class);
                int teamIndex = environment.fetchById(player.teamId).get(TeamInfoComponent.class).index;
                player.deathTime = engine.loop().time();
                beeing.remove(FlagCarrierComponent.class);
                environment.fetchSingletonComponent(HeavenComponent.class).deadPlayers.add(beeing);
                environment.remove(beeing);

                engine.ui().showNotification(NotificationDetails.text("%s has been killed".formatted(
                    InGameNotificationDesign.colorCode(player.name, teamIndex))));
            }
        }
    }
}
