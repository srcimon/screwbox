package io.github.srcimon.screwbox.examples.platformer.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.examples.platformer.components.TextComponent;
import io.github.srcimon.screwbox.examples.platformer.systems.BackToMenuSystem;
import io.github.srcimon.screwbox.examples.platformer.systems.PrintSystem;
import io.github.srcimon.screwbox.examples.platformer.systems.RestartGameSystem;

public class DeadScene implements Scene {

    @Override
    public void populate(Environment environment) {
        environment.addEntity(new TextComponent("GAME OVER", "press SPACE to restart"))
                .addSystem(new RestartGameSystem())
                .addSystem(new PrintSystem())
                .addSystem(new BackToMenuSystem());
    }

    @Override
    public void onEnter(Engine engine) {
        engine.window().setTitle("Platformer (Game Over)");
    }
}
