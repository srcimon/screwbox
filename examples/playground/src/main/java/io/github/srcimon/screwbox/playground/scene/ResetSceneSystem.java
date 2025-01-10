package io.github.srcimon.screwbox.playground.scene;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.ColorFadeAnimation;
import io.github.srcimon.screwbox.playground.scene.player.ControlKeys;

public class ResetSceneSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(ControlKeys.RESET)) {
            engine.scenes().resetActiveScene(SceneTransition.custom()
                    .outroDurationMillis(250)
                    .outroAnimation(new ColorFadeAnimation(Color.WHITE))
                    .introDurationMillis(250)
                    .introAnimation(new ColorFadeAnimation(Color.WHITE)));
        }
    }
}
