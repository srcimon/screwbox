package io.github.srcimon.screwbox.examples.pathfinding;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.examples.pathfinding.scenes.DemoScene;

public class PathfindingApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Pathfinding");

        engine.scenes().add(new DemoScene());

        engine.assets().enableLogging().prepareClassPackageAsync(PathfindingApp.class);

        engine.graphics().camera().setZoom(2.7);

        engine.start(DemoScene.class);
    }
}
