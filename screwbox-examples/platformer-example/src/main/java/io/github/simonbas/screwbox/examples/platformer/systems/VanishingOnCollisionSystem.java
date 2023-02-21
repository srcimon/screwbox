package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.audio.Sound;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.CollisionSensorComponent;
import io.github.simonbas.screwbox.core.entities.components.FadeOutComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.physics.Borders;
import io.github.simonbas.screwbox.core.utils.ListUtil;
import io.github.simonbas.screwbox.examples.platformer.components.MovingPlatformComponent;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerMarkerComponent;
import io.github.simonbas.screwbox.examples.platformer.components.VanishingOnCollisionComponent;

import java.util.List;

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
