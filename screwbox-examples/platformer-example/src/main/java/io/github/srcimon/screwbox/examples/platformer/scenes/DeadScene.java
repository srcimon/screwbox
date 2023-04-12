package io.github.srcimon.screwbox.examples.platformer.scenes;

import io.github.srcimon.screwbox.core.entities.Entities;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.examples.platformer.components.TextComponent;
import io.github.srcimon.screwbox.examples.platformer.systems.BackToMenuSystem;
import io.github.srcimon.screwbox.examples.platformer.systems.PrintSystem;
import io.github.srcimon.screwbox.examples.platformer.systems.RestartGameSystem;

public class DeadScene implements Scene {

    @Override
    public void initialize(Entities entities) {
        entities.add(new Entity().add(new TextComponent("GAME OVER", "press SPACE to restart")))
                .add(new RestartGameSystem())
                .add(new PrintSystem())
                .add(new BackToMenuSystem());
    }
}
