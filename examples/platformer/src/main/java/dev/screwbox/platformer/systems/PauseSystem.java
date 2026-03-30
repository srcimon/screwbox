package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.scenes.SceneTransition;
import dev.screwbox.core.scenes.transitions.SpriteFadeTransition;
import dev.screwbox.platformer.scenes.PauseScene;

import static dev.screwbox.core.graphics.options.SpriteDrawOptions.originalSize;

public class PauseSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (!engine.scenes().isTransitioning() && (engine.keyboard().isPressed(Key.P)
                || engine.keyboard().isPressed(Key.ESCAPE)
                || !engine.window().hasFocus())) {

            engine.audio().stopAllPlaybacks();
            engine.scenes().switchTo(PauseScene.class, SceneTransition.custom()
                    .introFilter(new SpriteFadeTransition(engine.graphics().screen().takeScreenshot()))
                    .introDurationMillis(500));
        }
    }

}
