package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.playground.debo.components.CurrentLevelComponent;
import de.suzufa.screwbox.playground.debo.scenes.GameScene;

public class RestartGameSystem implements EntitySystem {

    private static final Archetype CURRENT_LEVEL = Archetype.of(CurrentLevelComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isDown(Key.SPACE)) {
            EntityEngine entityEngine = engine.scenes().entityEngineOf(GameScene.class);
            String currentLevel = entityEngine.forcedFetch(CURRENT_LEVEL).get(CurrentLevelComponent.class).name;
            engine.scenes().remove(GameScene.class);
            engine.scenes().add(new GameScene(currentLevel));
            engine.scenes().switchTo(GameScene.class);
        }
    }

}
