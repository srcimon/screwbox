package de.suzufa.screwbox.examples.light;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.LightBlockingComponent;
import de.suzufa.screwbox.core.entityengine.components.LightEmitterComponent;
import de.suzufa.screwbox.core.entityengine.components.MagnetComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.systems.DynamicLightSystem;
import de.suzufa.screwbox.core.entityengine.systems.MagnetSystem;
import de.suzufa.screwbox.core.entityengine.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entityengine.systems.SpriteRenderSystem;
import de.suzufa.screwbox.core.graphics.Sprite;

public class LightExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine();

        Entity car = new Entity()
                .add(new LightBlockingComponent(-3))
                .add(new ColliderComponent(0, Percentage.of(0.4)))
                .add(new SpriteComponent(Sprite.fromFile("light/car.png")))
                .add(new PhysicsBodyComponent(Vector.zero()))
                .add(new AutoRotateSpriteComponent())
                .add(new TransformComponent(Bounds.atPosition(10, 25, 12, 18)));

        Entity light = new Entity()
                .add(new MagnetComponent(1, 10000))
                .add(new TransformComponent(Bounds.atOrigin(Vector.zero(), 4, 4)))
                .add(new LightEmitterComponent(600, Percentage.of(0.2)));

        engine.entityEngine()
                .add(new RenderBackgroundsSystem())
                .add(new SpriteRenderSystem())
                .add(new AutoRotateSpriteSystem())
                .add(new DynamicLightSystem())
                .add(new PhysicsSystem())
                .add(new MagnetSystem())
                .add(new MoveLightSourceSystem())
                .add(car)
                .add(light);

        engine.graphics().window().setTitle("Dynamic light example");
        engine.graphics().updateCameraZoom(2.5);
        engine.start();
    }

}
