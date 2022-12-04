package de.suzufa.screwbox.examples.platformer.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.examples.platformer.scenes.StartScene;

public class BackToMenuSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().justPressed(Key.ESCAPE)) {
            engine.scenes().switchTo(StartScene.class);
        }
    }

}
