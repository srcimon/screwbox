package de.suzufa.screwbox.playground.debo.systems;

import java.util.Optional;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.CameraMovementComponent;
import de.suzufa.screwbox.playground.debo.components.AutoflipByMovementComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

public class CameraShiftSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class);
    private static final Archetype CAMERA = Archetype.of(CameraMovementComponent.class);

    @Override
    public void update(Engine engine) {
        Optional<Entity> playerEntity = engine.entityEngine().fetch(PLAYER);
        if (playerEntity.isEmpty()) {
            return;
        }

        double delta = engine.loop().delta();
        Entity player = playerEntity.get();
        var cameraTrackerComponent = engine.entityEngine().forcedFetch(CAMERA).get(CameraMovementComponent.class);
        if (player.get(AutoflipByMovementComponent.class).flipped) {
            cameraTrackerComponent.shift = Vector.of(
                    Math.max(-50,
                            cameraTrackerComponent.shift.x() - cameraTrackerComponent.speed * delta * 100),
                    0);
        } else {
            cameraTrackerComponent.shift = Vector.of(
                    Math.min(50,
                            cameraTrackerComponent.shift.x() + cameraTrackerComponent.speed * delta * 100),
                    0);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.SIMULATION_BEGIN;
    }
}
