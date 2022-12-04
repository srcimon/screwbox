package de.suzufa.screwbox.examples.platformer.systems;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.SystemOrder;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.FadeOutComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.physics.Borders;
import de.suzufa.screwbox.core.utils.ListUtil;
import de.suzufa.screwbox.examples.platformer.components.MovingPlatformComponent;
import de.suzufa.screwbox.examples.platformer.components.PlayerMarkerComponent;
import de.suzufa.screwbox.examples.platformer.components.VanishingOnCollisionComponent;

@Order(SystemOrder.PREPARATION)
public class VanishingOnCollisionSystem implements EntitySystem {

    private static final Archetype VANISHINGS = Archetype.of(VanishingOnCollisionComponent.class,
            TransformComponent.class);
    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);
    private static final Asset<Sound> STEAM_SOUND = Sound.assetFromFile("sounds/steam.wav");

    @Override
    public void update(Engine engine) {
        Time now = engine.loop().lastUpdate();

        Entity player = engine.entities().forcedFetch(PLAYER);
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
                engine.audio().playEffect(STEAM_SOUND);
                vanish.vanishTime = now.plus(vanish.timeout);
            }
        }

        for (Entity vanishEntity : engine.entities().fetchAll(VANISHINGS)) {
            if (now.isAfter(vanishEntity.get(VanishingOnCollisionComponent.class).vanishTime)) {
                Vector center = vanishEntity.get(TransformComponent.class).bounds.position();
                Vector targetPosition = center.addY(200);
                vanishEntity.add(new FadeOutComponent(1))
                        .add(new CollisionSensorComponent())
                        .add(new MovingPlatformComponent(targetPosition, 20))
                        .remove(VanishingOnCollisionComponent.class);
            }
        }

    }
}
