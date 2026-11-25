package dev.screwbox.playground.gamescene.gamemodes.ctf;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.playground.gamescene.teams.PlayerComponent;

import java.util.List;

public class FlagScoreSystem implements EntitySystem {

    private static final Archetype FLAGS = Archetype.ofSpacial(FlagComponent.class);
    private static final Archetype PLAYERS = Archetype.ofSpacial(PlayerComponent.class);

    @Override
    public void update(Engine engine) {
        final List<Entity> players = engine.environment().fetchAll(PLAYERS);
        for (final var flagEntity : engine.environment().fetchAll(FLAGS)) {
            final var flag = flagEntity.get(FlagComponent.class);
            if (!flag.isStolen) {
                for (final var player : players) {
                    if (player.touches(flagEntity) && !player.get(PlayerComponent.class).team.equals(flag.team)) {
                        flag.carriedById = player.forceId();
                        flag.isStolen = true;
                        player.add(new FlagCarrierComponent());
                    }
                }
            } else {
                for (final var player : players) {
                    if (player.touches(flagEntity) && player.get(PlayerComponent.class).team.equals(flag.team)) {
                        flag.isStolen = false;
                        Entity homeZone = engine.environment().fetchById(flag.homeZoneId);
                        flagEntity.moveTo(homeZone.position());
                    }
                }
            }
            if (flag.carriedById != null) {
                engine.environment().tryFetchById(flag.carriedById).ifPresentOrElse(
                    carrier -> flagEntity.moveTo(carrier.position()),
                    () -> flagEntity.get(FlagComponent.class).carriedById = null);

            }
        }
    }
}
