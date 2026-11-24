package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.keyboard.DefaultControlSet;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.playground.design.InGameNotificationDesign;
import dev.screwbox.playground.design.SceneTransitions;
import dev.screwbox.playground.gamescene.GameScene;

import java.util.Set;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.ui()
            .setNotificationDesign(new InGameNotificationDesign())
            .setNotificationTimeout(Duration.ofSeconds(3));

        GameConfig gameConfig = new GameConfig(Set.of(Color.RED, Color.BLUE), 4, Duration.ofSeconds(2), Color.BLUE, "maps/ctf_turbine.json");
        screwBox.keyboard()
            .bindAlias(DefaultControlSet.UP, Key.W)
            .bindAlias(DefaultControlSet.DOWN, Key.S)
            .bindAlias(DefaultControlSet.LEFT, Key.A)
            .bindAlias(DefaultControlSet.RIGHT, Key.D);
        screwBox.assets()
            .enableLogging()
            .prepareEngineAssetsAsync()
            .prepareClassPackageAsync(PlaygroundApp.class);

        screwBox.scenes()
            .add(new GameScene(gameConfig))
            .switchTo(GameScene.class, SceneTransitions.BLEND_IN);

        screwBox.start();
    }

}