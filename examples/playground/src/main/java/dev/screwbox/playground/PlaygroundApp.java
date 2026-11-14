package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.playground.gamescene.GameScene;

import java.util.Set;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        GameConfig gameConfig = new GameConfig(Set.of(Color.RED, Color.BLUE), 4, Duration.ofSeconds(2));
        screwBox.scenes()
            .add(new GameScene(gameConfig))
            .switchTo(GameScene.class);

        screwBox.start();
    }

}