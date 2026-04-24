package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.PhysicsSystem;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class BoidSystemTest {

    @Test
    void update_noBoids_systemStopsProcessingFurther(DefaultEnvironment environment, Loop loop) {
        environment.addSystem(new BoidSystem());

        environment.update();

        verify(loop, never()).delta();
    }

    @Test
    void update_boidsPresent_appliesMotion(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.02);

        List<Entity> boids = createBoids();

        environment
            .addSystem(new BoidSystem())
            .addSystem(new PhysicsSystem())
            .addEntities(boids);

        environment.updateTimes(50);

        assertThat(boids).allSatisfy(boid -> assertThat(boid.get(PhysicsComponent.class).velocity.length()).isEqualTo(100, offset(0.01)));
    }

    @Test
    void update_obstaclesPresent_obstaclesAffectEntities(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.08);

        List<Entity> boids = createBoids();

        Entity container = new Entity()
            .bounds(Bounds.atOrigin(-200, -200, 400, 400))
            .add(new BoidObstacleComponent(), obstacle -> obstacle.isContainer = true);

        Entity obstacle = new Entity()
            .bounds(Bounds.atOrigin(10, 10, 20, 20))
            .add(new BoidObstacleComponent());

        environment
            .addSystem(new BoidSystem())
            .addSystem(new PhysicsSystem())
            .addEntities(boids)
            .addEntity(container)
            .addEntity(obstacle);

        environment.updateTimes(200);

        assertThat(boids)
            .allMatch(boid -> container.bounds().contains(boid.position()))
            .noneMatch(boid -> obstacle.bounds().contains(boid.position()));
    }

    private static List<Entity> createBoids() {
        return List.of(
            createBoid(10, 20),
            createBoid(15, 10),
            createBoid(20, 5),
            createBoid(2, 12));
    }

    private static Entity createBoid(double x, double y) {
        return new Entity().bounds(Bounds.$$(x, y, 20, 20))
            .add(new PhysicsComponent())
            .add(new BoidComponent(), config -> {
                config.obstacleAvoidanceStrength = 10;
                config.obstaclePerceptionRadius = 40;
            });
    }
}
