package de.suzufa.screwbox.examples.pathfinding;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.examples.pathfinding.scenes.DemoScene;

public class PathfindingExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Pathfinding Example");

        engine.scenes().add(new DemoScene("maze/map.json"));
        engine.loop().setTargetFps(99999);// TODO remove
        engine.start(DemoScene.class);
    }
//TODO: add LightDebugSystem()
    // TODO: this is the plan: engine.graphics().lightmap().addPointLight /
    // addNeonLight, add Spot, add ray

}
