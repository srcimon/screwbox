package de.suzufa.screwbox.playground.debo.systems;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entityengine.components.FadeOutComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.physics.Borders;
import de.suzufa.screwbox.core.utils.ListUtil;
import de.suzufa.screwbox.playground.debo.components.MovingPlattformComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.playground.debo.components.VanishingOnCollisionComponent;

public class VanishingOnCollisionSystem implements EntitySystem {

    private static final Archetype VANISHINGS = Archetype.of(VanishingOnCollisionComponent.class,
            TransformComponent.class);
    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);
    private static final Sound PFFFFF = Sound.fromFile("sounds/steam.wav");

    @Override
    public void update(Engine engine) {
        Time now = engine.loop().lastUpdate();

        Entity player = engine.entityEngine().forcedFetch(PLAYER);
        Bounds playerBounds = player.get(TransformComponent.class).bounds;

        List<Entity> activatedEntities = ListUtil.merge(
                engine.physics().raycastFrom(Vector.of(playerBounds.maxX() - 2, playerBounds.maxY()))
                        .ignoringEntities(player)
                        .checkingBorders(Borders.TOP_ONLY)
                        .checkingFor(VANISHINGS)
                        .castingVertical(0.5)
                        .selectAllEntities(),
                engine.physics().raycastFrom(Vector.of(playerBounds.minX() + 2, playerBounds.maxY()))
                        .ignoringEntities(player)
                        .checkingBorders(Borders.TOP_ONLY)
                        .checkingFor(VANISHINGS)
                        .castingVertical(0.5)
                        .selectAllEntities());

        for (final Entity entity : activatedEntities) {
            var vanish = entity.get(VanishingOnCollisionComponent.class);
            if (vanish.vanishTime.isUnset()) {
                engine.audio().playEffect(PFFFFF);
                vanish.vanishTime = now.plus(vanish.timeout);
            }
        }

        for (Entity vanishEntity : engine.entityEngine().fetchAll(VANISHINGS)) {
            if (now.isAfter(vanishEntity.get(VanishingOnCollisionComponent.class).vanishTime)) {
                Vector center = vanishEntity.get(TransformComponent.class).bounds.position();
                Vector targetPosition = center.addY(200);
                vanishEntity.add(new FadeOutComponent(1))
                        .add(new CollisionSensorComponent())
                        .add(new MovingPlattformComponent(targetPosition, 20))
                        .remove(VanishingOnCollisionComponent.class);
            }
        }

    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PREPARATION;
    }
}
