package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().configuration().setUseAntialiasing(true);
        screwBox.environment().addSystem(new DrawWaterSystem());
        screwBox.start();
    }
}