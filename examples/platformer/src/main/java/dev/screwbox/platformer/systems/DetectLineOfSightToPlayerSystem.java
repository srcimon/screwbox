package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.platformer.components.DetectLineOfSightToPlayerComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;

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
                    && engine.navigation()
                            .raycastFrom(detectorPosition)
                            .ignoringEntities(player)
                            .castingTo(playerPosition)
                            .noHit();
        }
    }
}
