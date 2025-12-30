package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.OptimizePhysicsPerformanceSystem;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class OptimizePhysicsPerformanceSystemTest {

    @Test
    void update_combinesHorizontallyAlignedColliders(DefaultEnvironment environment) {
        Entity brickA = new Entity().add(
                new StaticColliderComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(0, 0, 20, 20)));

        Entity brickB = new Entity().add(
                new StaticColliderComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(20, 0, 20, 20)));

        Entity brickC = new Entity().add(
                new StaticColliderComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(40, 0, 20, 20)));

        environment.add(brickA, brickB, brickC);
        environment.addSystem(new OptimizePhysicsPerformanceSystem());

        environment.update(); // one brick per cycle aligned
        environment.update(); // ...and another one

        var colliders = environment.fetchAll(Archetype.of(ColliderComponent.class));
        var bounds = colliders.getFirst().get(TransformComponent.class).bounds;
        assertThat(colliders).hasSize(1);
        assertThat(bounds).isEqualTo(Bounds.atOrigin(0, 0, 60, 20));
    }

    @Test
    void update_combinesVerticallyAlignedColliders(DefaultEnvironment environment) {
        Entity brickA = new Entity().add(
                new StaticColliderComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(0, 0, 20, 20)));

        Entity brickB = new Entity().add(
                new StaticColliderComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(0, 20, 20, 20)));

        Entity brickC = new Entity().add(
                new StaticColliderComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(0, 40, 20, 20)));

        environment.add(brickA, brickB, brickC);
        environment.addSystem(new OptimizePhysicsPerformanceSystem());

        environment.update(); // one brick per cycle aligned
        environment.update(); // ...and another one

        var colliders = environment.fetchAll(Archetype.of(ColliderComponent.class));
        var bounds = colliders.getFirst().get(TransformComponent.class).bounds;
        assertThat(colliders).hasSize(1);
        assertThat(bounds).isEqualTo(Bounds.atOrigin(0, 0, 20, 60));
    }

    @Test
    void update_ignoresDifferentColliders(DefaultEnvironment environment) {
        Entity brickA = new Entity().add(
                new StaticColliderComponent(),
                new ColliderComponent(4),
                new TransformComponent(Bounds.atOrigin(0, 0, 20, 20)));

        Entity brickB = new Entity().add(
                new StaticColliderComponent(),
                new ColliderComponent(2),
                new TransformComponent(Bounds.atOrigin(20, 0, 20, 20)));

        Entity brickC = new Entity().add(
                new StaticColliderComponent(),
                new ColliderComponent(),
                new TransformComponent(Bounds.atOrigin(40, 0, 20, 30)));

        environment.add(brickA, brickB, brickC);
        environment.addSystem(new OptimizePhysicsPerformanceSystem());

        environment.update();

        var colliders = environment.fetchAll(Archetype.of(ColliderComponent.class));
        assertThat(colliders).hasSize(3);
    }

    @Test
    void update_removesItselfAfterFinishingAllColliders(DefaultEnvironment environment) {
        environment.addSystem(new OptimizePhysicsPerformanceSystem());

        environment.update();

        assertThat(environment.systems()).isEmpty();
    }
}
