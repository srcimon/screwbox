package dev.screwbox.playground.softbody;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.fluids.FluidInteractionComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.playground.joints.JointStructureComponent;
import dev.screwbox.playground.joints.Joint;
import dev.screwbox.playground.joints.JointLinkComponent;

import java.util.ArrayList;
import java.util.List;

public class SoftbodyBuilder {

    private static final Sprite eye = Sprite.fromFile("eye.png");

    private SoftbodyBuilder() {

    }

    public static List<Entity> create(Vector position, Environment environment) {
        List<Entity> entities = new ArrayList<>();

        int i1 = environment.allocateId();
        int i2 = environment.allocateId();
        int i3 = environment.allocateId();
        int i4 = environment.allocateId();
        int i5 = environment.allocateId();
        int i6 = environment.allocateId();

        entities.add(new Entity(i1).bounds(Bounds.atPosition(position.add(16, 16), 4, 4))
                .add(new SoftbodyRenderComponent(Color.ORANGE))
                .add(new SoftbodyComponent())
                .add(new LeftRightControlComponent())
                .add(new JumpControlComponent())
                .add(new FluidInteractionComponent())
                .add(new JointLinkComponent(new Joint(i2)))
                .add(new JointStructureComponent(new Joint(i3)))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i2).bounds(Bounds.atPosition(position.add(16, 0), 4, 4))
                .add(new SoftbodyComponent())
                .add(new JointLinkComponent(new Joint(i3)))
                .add(new JointStructureComponent(new Joint(i4)))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i3).bounds(Bounds.atPosition(position.add(0, 0), 4, 4))
                .add(new SoftbodyComponent())
                .add(new JointLinkComponent(new Joint(i4)))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i4).bounds(Bounds.atPosition(position.add(0, 16), 4, 4))
                .add(new SoftbodyComponent())
                .add(new JumpControlComponent())
                .add(new LeftRightControlComponent())
                .add(new JointLinkComponent(new Joint(i1)))
                .add(new FloatComponent())
                .add(new FluidInteractionComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i5).bounds(Bounds.atPosition(position.add(12, 4), 4, 4))
                .add(new JointLinkComponent(new Joint(i3)))
                .add(new JointStructureComponent(new Joint(i4), new Joint(i2), new Joint(i1)))
                .add(new RenderComponent(eye, Order.DEBUG_OVERLAY.drawOrder()))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i6).bounds(Bounds.atPosition(position.add(2, 4), 4, 4))
                .add(new JointLinkComponent(new Joint(i3)))
                .add(new JointStructureComponent(new Joint(i4), new Joint(i2), new Joint(i1)))
                .add(new RenderComponent(eye, Order.DEBUG_OVERLAY.drawOrder()))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        return entities;
    }
}
