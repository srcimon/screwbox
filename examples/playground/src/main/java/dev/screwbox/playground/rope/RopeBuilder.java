package dev.screwbox.playground.rope;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.light.ConeGlowComponent;
import dev.screwbox.core.environment.light.ConeLightComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.particles.ParticleComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.playground.joint.Joint;
import dev.screwbox.playground.joint.SoftJointComponent;

public class RopeBuilder {


    public static void createRope(Environment environment, Bounds bounds) {
        double dist = -2;
        int max = 8;
        int abstand =6;
        int id = environment.allocateId();
        for (int i = max; i >= 0; i--) {
            Entity add = new Entity(id)
                    .name(i == max ? "start" : "node")
                    .add(new FloatComponent())
                    .add(new ParticleComponent())
                    .bounds(bounds.moveBy(0, dist + (abstand * max)).expand(-12))
                    .add(new PhysicsComponent(), p -> p.friction = 2);


            if (i == max) {
                add.add(new RopeComponent());
                add.add(new RopeRenderComponent(Color.ORANGE, 4));
                add.add(new ConeLightComponent(Angle.degrees(180), Angle.degrees(120), 180));
                add.add(new ConeGlowComponent(Angle.degrees(180), Angle.degrees(120), 180, Color.WHITE.opacity(0.3)));
                add.add(new GlowComponent(60, Color.WHITE.opacity(0.1)));
            }
            if (i != 0) {
                add.add(new SoftJointComponent((new Joint(environment.peekId()))));
            }
            environment.addEntity(add);
            dist -= abstand;
            id = environment.allocateId();
        }
    }
}
