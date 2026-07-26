package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.ChaoticMovementComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeAffectorComponent;
import dev.screwbox.core.environment.smoke.SmokeEmitterComponent;
import dev.screwbox.core.environment.smoke.SmokeSystem;
import dev.screwbox.core.environment.softphysics.SoftBodyBoundaryComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyCollisionComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.LineDrawOptions;

public class PlaygroundApp {

    static Color color = Color.WHITE;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().smoke().enable();
        screwBox.loop().unlockFps();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new SmokeSystem());

        for(int i = 0; i < 10; i++) {
            var soft = SoftPhysicsSupport.createSoftBody(Bounds.atPosition(-40*i, -40*i, 30,20), screwBox.environment());
            soft.root().add(new SoftBodyRenderComponent(Color.random().opacity(0.5))).add(new SoftBodyBoundaryComponent()).add(new SoftBodyCollisionComponent());
            soft.forEach(f -> f.add(new ChaoticMovementComponent(50, Duration.oneSecond())).add(new SmokeEmitterComponent(2, Color.YELLOW)));
            screwBox.environment().addEntities(soft);
        }
        screwBox.environment().addSystem(x -> {

            x.graphics().smoke().affect(screwBox.mouse().position(), range.multiply(screwBox.loop().delta()));
            x.graphics().smoke().emit(screwBox.mouse().position(), 400*screwBox.loop().delta(), color);
            if (x.mouse().isPressedLeft()) {
                color = Color.random();
            }
            if(x.mouse().isDownLeft()) {
                range = Angle.degrees(200*x.loop().delta()).rotate(range);
            }
            x.graphics().camera().move(x.keyboard().wsadMovement(2));
        });
        screwBox.environment().addEntity(new Entity().bounds(screwBox.graphics().visibleArea()).add(new BoidObstacleComponent(), c -> c.isContainer = true));

        screwBox.start();
    }

    static Vector range =Vector.y(-40);
}