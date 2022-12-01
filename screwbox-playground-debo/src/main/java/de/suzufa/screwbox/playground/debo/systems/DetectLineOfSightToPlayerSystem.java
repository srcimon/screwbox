package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.DetectLineOfSightToPlayerComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

@Order(UpdatePriority.PREPARATION)
public class DetectLineOfSightToPlayerSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class);
    private static final Archetype DETECTORS = Archetype.of(DetectLineOfSightToPlayerComponent.class);

    @Override
    public void update(Engine engine) {
        Entity player = engine.entities().forcedFetch(PLAYER);
        var playerPosition = player.get(TransformComponent.class).bounds.position();
        for (var detector : engine.entities().fetchAll(DETECTORS)) {
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
