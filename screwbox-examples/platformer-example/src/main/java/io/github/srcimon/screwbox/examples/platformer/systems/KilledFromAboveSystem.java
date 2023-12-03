package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.*;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.examples.platformer.components.KilledFromAboveComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;

import java.util.List;

import static io.github.srcimon.screwbox.core.utils.ListUtil.merge;

@Order(SystemOrder.PREPARATION)
public class KilledFromAboveSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(TransformComponent.class, PlayerMarkerComponent.class);
    private static final Archetype KILLED_FROM_ABOVE = Archetype.of(TransformComponent.class,
            KilledFromAboveComponent.class);

    @Override
    public void update(final Engine engine) {
        final Entity player = engine.ecosphere().forcedFetch(PLAYER);
        final var playerBounds = player.get(TransformComponent.class).bounds;

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
