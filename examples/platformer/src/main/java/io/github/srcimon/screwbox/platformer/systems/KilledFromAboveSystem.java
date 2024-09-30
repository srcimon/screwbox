package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.platformer.components.KilledFromAboveComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;

import java.util.List;

import static io.github.srcimon.screwbox.core.utils.ListUtil.merge;

@Order(Order.SystemOrder.PREPARATION)
public class KilledFromAboveSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(TransformComponent.class, PlayerMarkerComponent.class);
    private static final Archetype KILLED_FROM_ABOVE = Archetype.of(TransformComponent.class,
            KilledFromAboveComponent.class);

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
