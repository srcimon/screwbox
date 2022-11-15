package de.suzufa.screwbox.playground.debo.scenes;

import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.playground.debo.components.TextComponent;
import de.suzufa.screwbox.playground.debo.systems.BackToMenuSystem;
import de.suzufa.screwbox.playground.debo.systems.PrintSystem;
import de.suzufa.screwbox.playground.debo.systems.RestartGameSystem;

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
