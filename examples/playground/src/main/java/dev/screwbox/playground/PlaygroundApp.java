package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(3);
        engine.environment().enableAllFeatures()
                        .addSystemsFromPackage("dev.screwbox.playground");

        engine.start();
    }
}