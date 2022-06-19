package de.suzufa.screwbox.examples.pathfinding;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;

public class PathfindingExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine();

        new PathfindingMap("map.json").importInto(engine.entityEngine());

        engine.start();
    }

    // TODO: Physics.createFlowField(area).gridSize(16).towards(target-vector);

}
