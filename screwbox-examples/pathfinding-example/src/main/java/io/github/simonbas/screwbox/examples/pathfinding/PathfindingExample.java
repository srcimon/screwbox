package io.github.simonbas.screwbox.examples.pathfinding;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.examples.pathfinding.scenes.DemoScene;
import io.github.simonbas.screwbox.examples.pathfinding.scenes.LoadingScene;

public class PathfindingExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Pathfinding Example");

        engine.assets().preparePackage("io.github.simonbas.screwbox.examples.pathfinding");

        engine.scenes().add(new LoadingScene(), new DemoScene("map.json"));

        engine.start(LoadingScene.class);
    }

}
