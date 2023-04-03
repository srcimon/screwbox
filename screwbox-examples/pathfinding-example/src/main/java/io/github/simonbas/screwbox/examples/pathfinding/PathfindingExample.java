package io.github.simonbas.screwbox.examples.pathfinding;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.examples.pathfinding.scenes.DemoScene;

public class PathfindingExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Pathfinding Example");

        engine.assets().enableLogging().preparePackage("io.github.simonbas.screwbox.examples.pathfinding");

        engine.loop().setUnlimitedFps();
        engine.scenes().add(new DemoScene("map.json"));

        engine.start(DemoScene.class);
    }

}
