package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.navigation.Borders;
import dev.screwbox.platformer.components.DeathEventComponent;
import dev.screwbox.platformer.components.KilledFromAboveComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;

import java.util.List;

import static dev.screwbox.core.utils.ListUtil.merge;

@ExecutionOrder(Order.PREPARATION)
public class KilledFromAboveSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class);
    private static final Archetype KILLED_FROM_ABOVE = Archetype.ofSpacial(KilledFromAboveComponent.class);

    @Override
    public void update(final Engine engine) {
        final Entity player = engine.environment().fetchSingleton(PLAYER);
        final var playerBounds = player.bounds();

        engine.navigation()
            .raycastFrom(playerBounds.bottomLeft().addX(1))
            .checkingFor(KILLED_FROM_ABOVE)
            .checkingBorders(Borders.TOP)
            .castingVertical(4)
            .selectAllEntities()
            .forEach(entity -> entity.add(new DeathEventComponent()));

        engine.navigation()
            .raycastFrom(playerBounds.bottomRight().addX(-1))
            .checkingFor(KILLED_FROM_ABOVE)
            .checkingBorders(Borders.TOP)
            .castingVertical(4)
            .selectAllEntities()
            .forEach(entity -> entity.add(new DeathEventComponent()));
    }
}
