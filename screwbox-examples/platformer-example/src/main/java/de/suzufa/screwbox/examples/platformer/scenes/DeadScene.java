package de.suzufa.screwbox.examples.platformer.scenes;

import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.examples.platformer.components.TextComponent;
import de.suzufa.screwbox.examples.platformer.systems.BackToMenuSystem;
import de.suzufa.screwbox.examples.platformer.systems.PrintSystem;
import de.suzufa.screwbox.examples.platformer.systems.RestartGameSystem;

public class DeadScene implements Scene {

    @Override
    public void initialize(Entities entities) {
        Entity gameOverText = new Entity()
                .add(new TextComponent("GAME OVER", "press SPACE to restart"));

        entities
                .add(gameOverText)
                .add(new RestartGameSystem())
                .add(new PrintSystem())
                .add(new BackToMenuSystem());
    }
}
