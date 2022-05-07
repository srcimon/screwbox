package de.suzufa.screwbox.examples.light;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entityengine.systems.SpriteRenderSystem;
import de.suzufa.screwbox.core.graphics.Sprite;

public class LightExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine();

        Entity car = new Entity()
                .add(new SpriteComponent(Sprite.fromFile("light/car.png")))
                .add(new PhysicsBodyComponent(Vector.of(20, 40)))
                .add(new AutoRotateSpriteComponent())
                .add(new TransformComponent(Bounds.atPosition(Vector.of(40, 50), 12, 18)));

        engine.entityEngine()
                .add(new SpriteRenderSystem())
                .add(new AutoRotateSpriteSystem())
                .add(new PhysicsSystem())
                .add(car);

        engine.start();
    }
}
