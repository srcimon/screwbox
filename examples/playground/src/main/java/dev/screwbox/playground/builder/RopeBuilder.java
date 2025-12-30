package dev.screwbox.playground.builder;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.physics.AttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.Color;

public class RopeBuilder {

    public static void createRope(Environment environment, Vector start, Vector end, int count, int linkId) {
        Vector spacing = end.substract(start).multiply(1.0 / count);
        int id = environment.allocateId();
        for (int i = count; i >= 0; i--) {
            Entity add = new Entity(id)
                    .name(i == count ? "start" : "node")
                    .add(new FloatComponent())
                    .bounds(Bounds.atPosition(start.add(spacing.multiply(i)), 4, 4))
                    .add(new PhysicsComponent(), p -> p.friction = 2);


            if (i == count) {
                add.add(new RopeComponent());
                add.add(new RopeRenderComponent(Color.ORANGE.opacity(0.4), 2));
                add.add(new GlowComponent(20, Color.BLUE.opacity(0.1)));
                add.add(new PointLightComponent(80, Color.BLACK.opacity(0.8)));
            }
            if (i != 0) {

                add.add(new SoftLinkComponent(environment.peekId()));
            }
            if(i==0) {
                add.add(new AttachmentComponent(linkId));
            }
            environment.addEntity(add);
            id = environment.allocateId();
        }
    }
}
