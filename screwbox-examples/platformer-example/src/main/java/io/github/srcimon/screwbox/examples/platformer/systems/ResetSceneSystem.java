package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.ecosphere.Order;
import io.github.srcimon.screwbox.core.ecosphere.SystemOrder;
import io.github.srcimon.screwbox.examples.platformer.components.ResetSceneComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.DeadScene;

@Order(SystemOrder.SIMULATION_BEGIN)
public class ResetSceneSystem implements EntitySystem {

    private static final Archetype RESETTERS = Archetype.of(ResetSceneComponent.class);

    @Override
    public void update(Engine engine) {
        for (var resetter : engine.ecosphere().fetchAll(RESETTERS)) {
            Time resetTime = resetter.get(ResetSceneComponent.class).atTime;
            if (Time.now().isAfter(resetTime)) {
                engine.audio().stopAllSounds();
                engine.scenes().switchTo(DeadScene.class);
            }
        }
    }
}
