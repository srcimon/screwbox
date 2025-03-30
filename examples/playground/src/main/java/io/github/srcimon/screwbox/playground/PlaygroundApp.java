package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;

import static io.github.srcimon.screwbox.core.Bounds.$$;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().configuration().setUseAntialiasing(true);

        screwBox.environment()
                .addEntity(new Entity().name("gravity")
                        .add(new GravityComponent(Vector.y(300))))
                .addEntity(new Entity().name("box")
                        .add(new RenderComponent(SpriteBundle.BOX_STRIPED))
                        .add(new PhysicsComponent())
                        .bounds($$(0,-100, 32,32)))
                .addEntity(new Entity().name("water")
                        .bounds($$(-400, 0, 800, 80))
                        .add(new WaterComponent(20)))
                .addSystem(new DrawWaterSystem())
                .addSystem(new FloatSystem())
                .enableAllFeatures()
                .addSystem(new InteractWithWaterSystem())
                .addSystem(new UpdateWaterSystem());

        screwBox.start();
    }
}