package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.OptimizePhysicsPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.physics.StaticColliderComponent;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
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

        environment.addEntities(brickA, brickB, brickC);
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

        environment.addEntities(brickA, brickB, brickC);
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

        environment.addEntities(brickA, brickB, brickC);
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
