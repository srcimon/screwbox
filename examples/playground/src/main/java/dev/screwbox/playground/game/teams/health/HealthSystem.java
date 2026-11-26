package dev.screwbox.playground.game.teams.health;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.ui.NotificationDetails;
import dev.screwbox.playground.design.InGameNotificationDesign;
import dev.screwbox.playground.game.gamemodes.ctf.FlagCarrierComponent;
import dev.screwbox.playground.game.teams.HeavenComponent;
import dev.screwbox.playground.game.teams.PlayerComponent;
import dev.screwbox.playground.game.teams.TeamInfoComponent;

//TODO fix team usage here
public class HealthSystem implements EntitySystem {

    private static final Archetype LIVING = Archetype.of(HealthComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var living : engine.environment().fetchAll(LIVING)) {
            if (living.get(HealthComponent.class).health.isZero()) {
                final var player = living.get(PlayerComponent.class);
                int teamIndex = engine.environment().fetchById(player.teamId).get(TeamInfoComponent.class).index;
                player.deathTime = engine.loop().time();
                living.remove(FlagCarrierComponent.class);
                engine.environment().fetchSingletonComponent(HeavenComponent.class).deadPlayers.add(living);
                engine.environment().remove(living);

                engine.ui().showNotification(NotificationDetails.text("%s has been killed".formatted(
                    InGameNotificationDesign.colorByTeam(player.name, teamIndex))));
            }
        }
    }
}
