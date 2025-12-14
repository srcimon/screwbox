package dev.screwbox.playground.builder;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.fluids.FluidInteractionComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyCollisionComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyPressureComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class OldSoftbodyBuilder {

    private OldSoftbodyBuilder() {

    }

    public static List<Entity> createBall(Vector position, Environment environment, int nodes) {
        List<Entity> entities = new ArrayList<>();

        int id = environment.allocateId();
        int id2 = environment.allocateId();
        int id3 = environment.allocateId();
        int first = environment.peekId();
        entities.add(new Entity(id).add(new TransformComponent(position)).add(new PhysicsComponent(), p -> p.ignoreCollisions = true));
        entities.add(new Entity(id2).add(new TransformComponent(position.addY(20))).add(new PhysicsComponent(), p -> p.ignoreCollisions = true));
        entities.add(new Entity(id3).add(new TransformComponent(position.addX(30))).add(new PhysicsComponent(), p -> p.ignoreCollisions = true));

        for (int i = 0; i < nodes; i++) {
            var nodePos = Angle.circle(Percent.of(i * 1.0 / nodes)).applyOn(Line.normal(position, 10)).end();
            Entity circleNode = new Entity(environment.allocateId())
                    .add(new TransformComponent(nodePos, 2, 2))
                    .add(new PhysicsComponent(), p -> p.friction = 2)
                    .add(new FloatComponent())
                    .add(new SoftStructureComponent(id, id2, id3))
                    .add(new SoftLinkComponent(i == nodes - 1 ? first : environment.peekId()));
            if (i == 0) {
                circleNode
                        .add(new SoftBodyComponent())
                        .add(new SoftBodyPressureComponent(0))
                        .add(new SoftBodyCollisionComponent())
                        .add(new SoftBodyRenderComponent(Color.RED.opacity(0.5)));
            }
            entities.add(circleNode);
        }
        return entities;
    }

    public static List<Entity> create(Vector position, Environment environment) {
        List<Entity> entities = new ArrayList<>();
        int size = 16;
        int i1 = environment.allocateId();
        int i2 = environment.allocateId();
        int i3 = environment.allocateId();
        int i4 = environment.allocateId();
        int i5 = environment.allocateId();
        int i6 = environment.allocateId();

        entities.add(new Entity(i1).bounds(Bounds.atPosition(position.add(0, 0), 4, 4))
                .add(new SoftBodyRenderComponent(Color.ORANGE.opacity(0.5)), config -> {
                    config.outlineColor = Color.ORANGE;
                    config.outlineStrokeWidth = 4;
                })
                .add(new SoftBodyComponent())
                .add(new SoftBodyPressureComponent(20))
                .add(new SoftBodyCollisionComponent())
                .add(new LeftRightControlComponent())
                .add(new JumpControlComponent())
                .add(new FluidInteractionComponent())
                .add(new SoftLinkComponent(i2))
                .add(new SoftStructureComponent(i3, i4, i5))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i2).bounds(Bounds.atPosition(position.add(size, 0), 4, 4))
                .add(new SoftLinkComponent(i3))
                .add(new SoftStructureComponent(i4, i5, i6))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i3).bounds(Bounds.atPosition(position.add(size * 2.0 + 4, 0), 4, 4))
                .add(new SoftLinkComponent(i4))
                .add(new SoftStructureComponent(i5, i6))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i4).bounds(Bounds.atPosition(position.add(size * 2.0 + 4, size), 4, 4))
                .add(new JumpControlComponent())
                .add(new LeftRightControlComponent())
                .add(new SoftLinkComponent(i5))
                .add(new FloatComponent())
                .add(new FluidInteractionComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i5).bounds(Bounds.atPosition(position.add(size, size), 4, 4))
                .add(new JumpControlComponent())
                .add(new LeftRightControlComponent())
                .add(new SoftLinkComponent(i6))
                .add(new FloatComponent())
                .add(new FluidInteractionComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        entities.add(new Entity(i6).bounds(Bounds.atPosition(position.add(0, size), 4, 4))
                .add(new JumpControlComponent())
                .add(new LeftRightControlComponent())
                .add(new SoftLinkComponent(i1))
                .add(new FloatComponent())
                .add(new FluidInteractionComponent())
                .add(new PhysicsComponent(), p -> p.friction = 2));

        return entities;
    }
}
