package dev.screwbox.platformer.systems;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenDestroyComponent;
import dev.screwbox.core.environment.tweening.TweenOpacityComponent;
import dev.screwbox.core.navigation.Borders;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.platformer.components.MovingPlatformComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.platformer.components.VanishingOnCollisionComponent;

import java.util.List;

import static dev.screwbox.core.Duration.ofMillis;

@ExecutionOrder(Order.PREPARATION)
public class VanishingOnCollisionSystem implements EntitySystem {

    private static final Archetype VANISHINGS = Archetype.ofSpacial(VanishingOnCollisionComponent.class);
    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class);

    @Override
    public void update(Engine engine) {
        Time now = engine.loop().time();

        Entity player = engine.environment().fetchSingleton(PLAYER);
        Bounds playerBounds = player.bounds();

        List<Entity> activatedEntities = ListUtil.merge(

                engine.navigation().raycastFrom(playerBounds.bottomRight().addX(-2))
                        .ignoringEntities(player)
                        .checkingBorders(Borders.TOP_ONLY)
                        .checkingFor(VANISHINGS)
                        .castingVertical(0.5)
                        .selectAllEntities(),
                engine.navigation().raycastFrom(playerBounds.bottomLeft().addX(2))
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
                        .add(new CollisionSensorComponent())
                        .add(new MovingPlatformComponent(targetPosition, 20))
                        .remove(VanishingOnCollisionComponent.class);
            }
        }

    }
}
