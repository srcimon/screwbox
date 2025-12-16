package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.GravitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.PhysicsSystem;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SoftBodyCollisionSystemTest {

    //TODO test collisions
    @Test
    void update_collidingSoftBodiesPresent_preventsFallingThrough(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.0005);

        environment
                .addSystem(new SoftBodySystem())
                .addSystem(new PhysicsSystem())
                .addSystem(new SoftPhysicsSystem())
                .addSystem(new SoftBodyCollisionSystem())
                .addSystem(new GravitySystem())

                .addEntity(new Entity().name("gravity")
                        .add(new GravityComponent(Vector.y(150))))

                .addEntity(new Entity().name("floor")
                        .add(new TransformComponent(Bounds.atOrigin(0, 0, 400, 400)))
                        .add(new ColliderComponent()))

                .addEntity(new Entity(1).name("jelly-bottom-box")
                        .add(new SoftBodyComponent())
                        .add(new SoftBodyCollisionComponent())
                        .add(new TransformComponent(Bounds.atOrigin(40, -40, 4, 4)))
                        .add(new SoftStructureComponent(3))
                        .add(new SoftLinkComponent(2))
                        .add(new PhysicsComponent()))
                .addEntity(new Entity(2).name("jelly-bottom-box-node")
                        .add(new TransformComponent(Bounds.atOrigin(80, -40, 4, 4)))
                        .add(new SoftLinkComponent(3))
                        .add(new SoftStructureComponent(4))
                        .add(new PhysicsComponent()))
                .addEntity(new Entity(3).name("jelly-bottom-box-node")
                        .add(new TransformComponent(Bounds.atOrigin(80, -10, 4, 4)))
                        .add(new SoftLinkComponent(4))
                        .add(new PhysicsComponent()))
                .addEntity(new Entity(4).name("jelly-bottom-box-node")
                        .add(new TransformComponent(Bounds.atOrigin(40, -10, 4, 4)))
                        .add(new SoftLinkComponent(1))
                        .add(new PhysicsComponent()))

                .addEntity(new Entity(11).name("jelly-top-box")
                        .add(new SoftBodyComponent())
                        .add(new SoftBodyCollisionComponent())
                        .add(new TransformComponent(Bounds.atOrigin(40, -80, 4, 4)))
                        .add(new SoftLinkComponent(12))
                        .add(new SoftStructureComponent(13))
                        .add(new PhysicsComponent()))
                .addEntity(new Entity(12).name("jelly-top-box-node")
                        .add(new TransformComponent(Bounds.atOrigin(80, -80, 4, 4)))
                        .add(new SoftLinkComponent(13))
                        .add(new SoftStructureComponent(14))
                        .add(new PhysicsComponent()))
                .addEntity(new Entity(13).name("jelly-top-box-node")
                        .add(new TransformComponent(Bounds.atOrigin(80, -50, 4, 4)))
                        .add(new SoftLinkComponent(14))
                        .add(new PhysicsComponent()))
                .addEntity(new Entity(14).name("jelly-top-box-node")
                        .add(new TransformComponent(Bounds.atOrigin(40, -50, 4, 4)))
                        .add(new SoftLinkComponent(11))
                        .add(new PhysicsComponent()));

        environment.updateTimes(50);

        Vector topBoxPosition = environment.fetchById(1).position();
        assertThat(topBoxPosition.y()).isBetween(-40.0, -30.0);

        Vector bottomBoxPosition = environment.fetchById(11).position();
        assertThat(bottomBoxPosition.y()).isBetween(-80.0, -70.0);
    }
}
