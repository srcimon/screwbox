package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.controls.JumpControlComponent;
import io.github.srcimon.screwbox.core.environment.controls.LeftRightControlComponent;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.physics.FloatComponent;
import io.github.srcimon.screwbox.core.environment.physics.FluidComponent;
import io.github.srcimon.screwbox.core.environment.physics.FluidInteractionComponent;
import io.github.srcimon.screwbox.core.environment.rendering.FluidRenderComponent;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;

import static io.github.srcimon.screwbox.core.Bounds.$$;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.environment()
                .addEntity(new Entity().name("gravity")
                        .add(new GravityComponent(Vector.y(600))))

                .addEntity(new Entity().name("box")
                        .add(new RenderComponent(SpriteBundle.BOX_STRIPED))
                        .add(new FloatComponent(400, 500))
                        .add(new FluidInteractionComponent())
                        .add(new PhysicsComponent())
                        .add(new JumpControlComponent())
                        .add(new LeftRightControlComponent())
                        .bounds($$(0, -200, 32, 32)))

                .addEntity(new Entity().name("water")
                        .bounds($$(-400, 0, 800, 300))
                        .add(new FluidComponent(32))
                        .add(new FluidRenderComponent()))

                .enableAllFeatures()
                .addSystem(new LogFpsSystem());

        engine.start();
    }
}