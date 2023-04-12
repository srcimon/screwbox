package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entities;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.platformer.components.CurrentLevelComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.GameScene;

public class RestartGameSystem implements EntitySystem {

    private static final Archetype CURRENT_LEVEL = Archetype.of(CurrentLevelComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isDown(Key.SPACE)) {
            Entities entities = engine.scenes().entitiesOf(GameScene.class);
            String currentLevel = entities.forcedFetch(CURRENT_LEVEL).get(CurrentLevelComponent.class).name;
            engine.scenes().remove(GameScene.class);
            engine.scenes().add(new GameScene(currentLevel));
            engine.scenes().switchTo(GameScene.class);
        }
    }

}
