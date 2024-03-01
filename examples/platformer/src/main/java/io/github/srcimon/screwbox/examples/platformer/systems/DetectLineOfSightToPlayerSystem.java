package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.examples.platformer.components.DetectLineOfSightToPlayerComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;

@Order(SystemOrder.PREPARATION)
public class DetectLineOfSightToPlayerSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class);
    private static final Archetype DETECTORS = Archetype.of(DetectLineOfSightToPlayerComponent.class);

    @Override
    public void update(Engine engine) {
        Entity player = engine.environment().fetch(PLAYER);
        var playerPosition = player.get(TransformComponent.class).bounds.position();
        for (var detector : engine.environment().fetchAll(DETECTORS)) {
            var detectorPosition = detector.get(TransformComponent.class).bounds.position();
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
