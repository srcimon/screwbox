package io.github.srcimon.screwbox.platformer.scenes;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.scenes.Scene;
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
                .enableRendering()
                .addEntity(new TextComponent("GAME OVER", "press SPACE to restart"))
                .addEntity(new CurrentLevelComponent(mapName))
                .addSystem(new RestartGameSystem())
                .addSystem(new PrintSystem())
                .addSystem(new BackToMenuSystem());
    }

    @Override
    public void onEnter(Engine engine) {
        engine.audio().stopAllPlaybacks();
        engine.graphics().disableSplitScreenMode();
    }
}
