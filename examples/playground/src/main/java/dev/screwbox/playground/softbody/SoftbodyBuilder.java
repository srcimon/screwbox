package dev.screwbox.playground.softbody;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.fluids.FluidInteractionComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class SoftbodyBuilder {

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

        entities.add(new Entity(i1).bounds(Bounds.atPosition(position.add(0, 0), 4, 4))
                .add(new SoftbodyRenderComponent(Color.ORANGE.opacity(0.5)), config -> {
                    config.outlineColor = Color.ORANGE;
                    config.outlineStrokeWidth = 4;
                })
                .add(new SoftBodyComponent())
                .add(new LeftRightControlComponent())
                .add(new JumpControlComponent())
                .add(new FluidInteractionComponent())
                .add(new SoftLinkComponent(i2))
                .add(new SoftStructureComponent(i3, i4, i5))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i2).bounds(Bounds.atPosition(position.add(16, 0), 4, 4))
                .add(new SoftLinkComponent(i3))
                .add(new SoftStructureComponent(i4, i5, i6))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i3).bounds(Bounds.atPosition(position.add(32, 0), 4, 4))
                .add(new SoftLinkComponent(i4))
                .add(new SoftStructureComponent(i5, i6))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i4).bounds(Bounds.atPosition(position.add(32, 16), 4, 4))
                .add(new JumpControlComponent())
                .add(new LeftRightControlComponent())
                .add(new SoftLinkComponent(i5))
                .add(new FloatComponent())
                .add(new FluidInteractionComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i5).bounds(Bounds.atPosition(position.add(16, 16), 4, 4))
                .add(new JumpControlComponent())
                .add(new LeftRightControlComponent())
                .add(new SoftLinkComponent(i6))
                .add(new FloatComponent())
                .add(new FluidInteractionComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i6).bounds(Bounds.atPosition(position.add(0, 16), 4, 4))
                .add(new JumpControlComponent())
                .add(new LeftRightControlComponent())
                .add(new SoftLinkComponent(i1))
                .add(new FloatComponent())
                .add(new FluidInteractionComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        return entities;
    }
}
