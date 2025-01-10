package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.playground.scene.PlaygroundScene;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.scenes()
                .add(new PlaygroundScene())
                .switchTo(PlaygroundScene.class);

        screwBox.start();
    }
}