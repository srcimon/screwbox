package io.github.srcimon.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.scenes().add(new PlaygroundScene())
                .switchTo(PlaygroundScene.class);

        engine.assets()
                .enableLogging()
                .prepareClassPackageAsync(PlaygroundApp.class);

        engine.start();
    }
}