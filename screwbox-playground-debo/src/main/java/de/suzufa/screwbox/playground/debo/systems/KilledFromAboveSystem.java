package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.utils.ListUtil.merge;

import java.util.List;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.physics.Borders;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent;
import de.suzufa.screwbox.playground.debo.components.KilledFromAboveComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

@Order(UpdatePriority.PREPARATION)
public class KilledFromAboveSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(TransformComponent.class, PlayerMarkerComponent.class);
    private static final Archetype KILLED_FROM_ABOVE = Archetype.of(TransformComponent.class,
            KilledFromAboveComponent.class);

    @Override
    public void update(Engine engine) {
        Entity player = engine.entities().forcedFetch(PLAYER);
        var playerBounds = player.get(TransformComponent.class).bounds;

        var left = Vector.of(playerBounds.minX() + 1, playerBounds.maxY());
        var right = Vector.of(playerBounds.maxX() - 1, playerBounds.maxY());

        List<Entity> enemiesBelow = merge(
                engine.physics()
                        .raycastFrom(left)
                        .checkingFor(KILLED_FROM_ABOVE)
                        .checkingBorders(Borders.TOP_ONLY)
                        .castingVertical(4)
                        .selectAllEntities(),
                engine.physics()
                        .raycastFrom(right)
                        .checkingFor(KILLED_FROM_ABOVE)
                        .checkingBorders(Borders.TOP_ONLY)
                        .castingVertical(4)
                        .selectAllEntities());

        for (var entity : enemiesBelow) {
            entity.add(new DeathEventComponent());
        }
    }
}
