package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.CameraMovementComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.graphics.Flip;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerMarkerComponent;

import java.util.Optional;

@Order(SystemOrder.SIMULATION_BEGIN)
public class CameraShiftSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, RenderComponent.class);
    private static final Archetype CAMERA = Archetype.of(CameraMovementComponent.class);

    @Override
    public void update(Engine engine) {
        Optional<Entity> playerEntity = engine.entities().fetch(PLAYER);
        if (playerEntity.isEmpty()) {
            return;
        }

        double delta = engine.loop().delta();
        Entity player = playerEntity.get();
        var cameraTrackerComponent = engine.entities().forcedFetch(CAMERA).get(CameraMovementComponent.class);
        if (Flip.HORIZONTAL.equals(player.get(RenderComponent.class).flip)) {
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
}
