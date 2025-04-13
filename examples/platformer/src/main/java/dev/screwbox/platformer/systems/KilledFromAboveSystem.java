package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.physics.Borders;
import dev.screwbox.platformer.components.DeathEventComponent;
import dev.screwbox.platformer.components.KilledFromAboveComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;

import java.util.List;

import static dev.screwbox.core.utils.ListUtil.merge;

@Order(Order.SystemOrder.PREPARATION)
public class KilledFromAboveSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class);
    private static final Archetype KILLED_FROM_ABOVE = Archetype.ofSpacial(KilledFromAboveComponent.class);

    @Override
    public void update(final Engine engine) {
        final Entity player = engine.environment().fetchSingleton(PLAYER);
        final var playerBounds = player.bounds();

        final List<Entity> enemiesBelow = merge(
                engine.physics()
                        .raycastFrom(playerBounds.bottomLeft().addX(1))
                        .checkingFor(KILLED_FROM_ABOVE)
                        .checkingBorders(Borders.TOP_ONLY)
                        .castingVertical(4)
                        .selectAllEntities(),
                engine.physics()
                        .raycastFrom(playerBounds.bottomRight().addX(-1))
                        .checkingFor(KILLED_FROM_ABOVE)
                        .checkingBorders(Borders.TOP_ONLY)
                        .castingVertical(4)
                        .selectAllEntities());

        for (final var entity : enemiesBelow) {
            entity.add(new DeathEventComponent());
        }
    }
}
