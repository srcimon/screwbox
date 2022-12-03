package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.utils.ListUtil.merge;

import java.util.List;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.SystemOrder;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.physics.Borders;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent;
import de.suzufa.screwbox.playground.debo.components.KilledFromAboveComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

@Order(SystemOrder.PREPARATION)
public class KilledFromAboveSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(TransformComponent.class, PlayerMarkerComponent.class);
    private static final Archetype KILLED_FROM_ABOVE = Archetype.of(TransformComponent.class,
            KilledFromAboveComponent.class);

    @Override
    public void update(final Engine engine) {
        final Entity player = engine.entities().forcedFetch(PLAYER);
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
