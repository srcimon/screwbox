package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.Order;
import io.github.simonbas.screwbox.core.entities.SystemOrder;
import io.github.simonbas.screwbox.examples.platformer.components.ResetSceneComponent;
import io.github.simonbas.screwbox.examples.platformer.scenes.DeadScene;

@Order(SystemOrder.SIMULATION_BEGIN)
public class ResetSceneSystem implements EntitySystem {

    private static final Archetype RESETTERS = Archetype.of(ResetSceneComponent.class);

    @Override
    public void update(Engine engine) {
        for (var resetter : engine.entities().fetchAll(RESETTERS)) {
            Time resetTime = resetter.get(ResetSceneComponent.class).atTime;
            if (Time.now().isAfter(resetTime)) {
                engine.audio().stopAllSounds();
                engine.scenes().switchTo(DeadScene.class);
            }
        }
    }
}
