package de.suzufa.screwbox.examples.platformer.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.SystemOrder;
import de.suzufa.screwbox.examples.platformer.components.ResetSceneComponent;
import de.suzufa.screwbox.examples.platformer.scenes.DeadScene;

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
