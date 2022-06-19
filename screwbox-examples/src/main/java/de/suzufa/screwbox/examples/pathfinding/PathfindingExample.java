package de.suzufa.screwbox.examples.pathfinding;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entityengine.systems.AutoRotationSystem;
import de.suzufa.screwbox.core.entityengine.systems.CameraMovementSystem;
import de.suzufa.screwbox.core.entityengine.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entityengine.systems.SpriteRenderSystem;

public class PathfindingExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine();

        new PathfindingMap("map.json").importInto(engine.entityEngine());

        engine.entityEngine()
                .add(new SpriteRenderSystem())
                .add(new CameraMovementSystem())
                .add(new PlayerMovementSystem())
                .add(new AutoRotationSystem())
                .add(new PhysicsSystem());

        engine.start();
    }

    // TODO: Physics.createFlowField(area).gridSize(16).towards(target-vector);

}
