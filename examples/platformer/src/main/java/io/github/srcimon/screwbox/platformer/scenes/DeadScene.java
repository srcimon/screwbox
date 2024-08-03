package io.github.srcimon.screwbox.platformer.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.platformer.components.CurrentLevelComponent;
import io.github.srcimon.screwbox.platformer.components.TextComponent;
import io.github.srcimon.screwbox.platformer.systems.BackToMenuSystem;
import io.github.srcimon.screwbox.platformer.systems.PrintSystem;
import io.github.srcimon.screwbox.platformer.systems.RestartGameSystem;

public class DeadScene implements Scene {

    private final String mapName;

    public DeadScene(final String mapName) {
        this.mapName = mapName;
    }
    @Override
    public void populate(Environment environment) {
        environment
                .addEntity(new TextComponent("GAME OVER", "press SPACE to restart"))
                .addEntity(new CurrentLevelComponent(mapName))
                .addSystem(new RestartGameSystem())
                .addSystem(new PrintSystem())
                .addSystem(new BackToMenuSystem());
    }

    @Override
    public void onEnter(Engine engine) {
        engine.audio().stopAllSounds();
        engine.window().setTitle("Platformer (Game Over)");
    }
}
