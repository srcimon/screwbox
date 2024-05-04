package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.core.utils.ListUtil;
import io.github.srcimon.screwbox.examples.platformer.components.MovingPlatformComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;
import io.github.srcimon.screwbox.examples.platformer.components.VanishingOnCollisionComponent;

import java.util.List;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;

@Order(SystemOrder.PREPARATION)
public class VanishingOnCollisionSystem implements EntitySystem {

    private static final Archetype VANISHINGS = Archetype.of(VanishingOnCollisionComponent.class,
            TransformComponent.class);
    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        Time now = engine.loop().lastUpdate();

        Entity player = engine.environment().fetchSingleton(PLAYER);
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
                engine.audio().playSound(SoundBundle.STEAM);
                vanish.vanishTime = vanish.timeout.addTo(now);
            }
        }

        for (Entity vanishEntity : engine.environment().fetchAll(VANISHINGS)) {
            if (now.isAfter(vanishEntity.get(VanishingOnCollisionComponent.class).vanishTime)) {
                Vector center = vanishEntity.position();
                Vector targetPosition = center.addY(200);
                vanishEntity
                        .add(new TweenComponent(ofMillis(400)))
                        .add(new TweenOpacityComponent())
                        .add(new TweenDestroyComponent())
                        .add(new CollisionDetectionComponent())
                        .add(new MovingPlatformComponent(targetPosition, 20))
                        .remove(VanishingOnCollisionComponent.class);
            }
        }

    }
}
