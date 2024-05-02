package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.DefaultScene;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;

public class AltScene implements Scene {
    @Override
    public void populate(Environment environment) {
        environment.addSystem(engine -> engine.graphics().screen().fillWith(Color.RED))
         .addSystem(engine -> {
            if(engine.keyboard().isPressed(Key.SPACE)) {
                engine.scenes().switchTo(DefaultScene.class, SceneTransition
                        .extro(new SceneTransition.ExtroAnimation() {
                            @Override
                            public void draw(Screen screen, Percent progress) {
                                screen.fillWith(Color.BLUE.opacity(progress));
                            }
                        }, Duration.ofMillis(1200)));
            }
        });
    }
}
