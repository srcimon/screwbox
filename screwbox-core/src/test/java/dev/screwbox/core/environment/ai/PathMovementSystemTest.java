package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.TailwindComponent;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class PathMovementSystemTest {

    @Test
    void update_noPath_noMovement(DefaultEnvironment environment) {
        var physics = new PhysicsComponent();

        environment
                .addEntity(new Entity()
                        .add(physics)
                        .add(new TransformComponent())
                        .add(new PathMovementComponent(20, 40)))
                .addSystem(new PathMovementSystem());

        environment.update();

        assertThat(physics.velocity).isEqualTo(Vector.zero());
    }

    @Test
    void update_nearTarget_switchesToNextTarget(DefaultEnvironment environment) {
        var physics = new PhysicsComponent();
        var movement = new PathMovementComponent(20, 40);
        Entity mover = new Entity();

        environment
                .addEntity(mover
                        .add(new TransformComponent())
                        .add(physics)
                        .add(movement, config -> config.path = Polygon.ofNodes(List.of(Vector.x(0.5), Vector.x(22.5)))))
                .addSystem(new PathMovementSystem());

        environment.update();

        assertThat(physics.velocity).isEqualTo(Vector.zero());
        assertThat(movement.path.nodes()).containsExactly(Vector.x(22.5));
        assertThat(mover.get(TargetMovementComponent.class).position).isEqualTo(Vector.x(22.5));
    }
}
