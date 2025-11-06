package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.playground.joint.Joint;
import dev.screwbox.playground.joint.JointComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SoftbodyBuilder {

    private static final Random RANDOM = new Random();

    public static List<Entity> create(Vector position) {
        List<Entity> entities = new ArrayList<>();

        int i1 = randomId();
        int i2 = randomId();
        int i3 = randomId();
        int i4 = randomId();

        entities.add(new Entity(i1).bounds(Bounds.atPosition(position.add(40, 40), 4, 4))
                .add(new JointComponent(List.of(new Joint(i2), new Joint(i3))))
                        .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 200));

        entities.add(new Entity(i2).bounds(Bounds.atPosition(position.add(40, 0), 4, 4))
                .add(new JointComponent(List.of(new Joint(i3), new Joint(i4))))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 200));

        entities.add(new Entity(i3).bounds(Bounds.atPosition(position.add(0, 0), 4, 4))
                .add(new JointComponent(List.of(new Joint(i4))))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 200));

        entities.add(new Entity(i4).bounds(Bounds.atPosition(position.add(0, 40), 4, 4))
                .add(new JointComponent(List.of(new Joint(i1))))
                .add(new FloatComponent())
                .add(new PhysicsComponent(), p -> p.friction = 200));

        return entities;
    }

    private static int randomId() {
        return RANDOM.nextInt(-1000000, -100);
    }
}
