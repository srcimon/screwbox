package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.platformer.components.DetectLineOfSightToPlayerComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;

@Order(Order.SystemOrder.PREPARATION)
public class DetectLineOfSightToPlayerSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class);
    private static final Archetype DETECTORS = Archetype.of(DetectLineOfSightToPlayerComponent.class);

    @Override
    public void update(Engine engine) {
        Entity player = engine.environment().fetchSingleton(PLAYER);
        var playerPosition = player.position();
        for (var detector : engine.environment().fetchAll(DETECTORS)) {
            var detectorPosition = detector.position();
            var detectorComponent = detector.get(DetectLineOfSightToPlayerComponent.class);
            detectorComponent.isInLineOfSight = detectorPosition
                    .distanceTo(playerPosition) < detectorComponent.maxDitance
                    && engine.physics()
                            .raycastFrom(detectorPosition)
                            .ignoringEntities(player)
                            .castingTo(playerPosition)
                            .noHit();
        }
    }
}
