package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.SpriteFadeAnimation;
import io.github.srcimon.screwbox.platformer.scenes.PauseScene;

import static io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions.originalSize;

public class PauseSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (!engine.scenes().isTransitioning() && (engine.keyboard().isPressed(Key.P)
                || engine.keyboard().isPressed(Key.ESCAPE)
                || !engine.window().hasFocus())) {

            engine.audio().stopAllPlaybacks();

            SpriteFadeAnimation animation = new SpriteFadeAnimation(engine.graphics().screen().takeScreenshot(),
                    originalSize().rotation(engine.graphics().screen().rotation().invert()));
            engine.scenes().switchTo(PauseScene.class, SceneTransition.custom()
                    .introAnimation(animation)
                    .introDurationMillis(500));
        }
    }

}
