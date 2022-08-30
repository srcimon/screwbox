package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.playground.debo.components.ResetSceneComponent;
import de.suzufa.screwbox.playground.debo.scenes.DeadScene;

public class ResetSceneSystem implements EntitySystem {

    private static final Archetype RESETTERS = Archetype.of(ResetSceneComponent.class);

    @Override
    public void update(Engine engine) {
        for (var resetter : engine.entityEngine().fetchAll(RESETTERS)) {
            Time resetTime = resetter.get(ResetSceneComponent.class).atTime;
            if (Time.now().isAfter(resetTime)) {
                engine.audio().stopAllSounds();
                engine.scenes().switchTo(DeadScene.class);
            }
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.SIMULATION_BEGIN;
    }
}
