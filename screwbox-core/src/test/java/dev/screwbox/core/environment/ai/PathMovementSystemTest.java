package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.async.Async;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.navigation.Navigation;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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

    @Test
    void update_targetEntityFoundAndHasPath_updatesPath(DefaultEnvironment environment, Async async, Loop loop, Navigation navigation) {
        when(loop.time()).thenReturn(Time.now());
        when(navigation.findPath($(0, 0), $(100, 0))).thenReturn(Optional.of(Polygon.ofNodes($(0, 0), $(100, 0))));
        var movementComponent = new PathMovementComponent(100, 200);
        environment
            .addSystem(new PathMovementSystem())
            .addEntity(new Entity(12)
                .add(new TransformComponent($(100, 0))))
            .addEntity(new Entity(2)
                .add(new PhysicsComponent())
                .add(new TransformComponent($(0, 0)))
                .add(movementComponent, movement -> movement.targetId = 12)
            );

        ArgumentCaptor<Runnable> asyncTask = ArgumentCaptor.forClass(Runnable.class);

        when(async.runExclusive(eq(movementComponent), asyncTask.capture())).thenReturn(async);

        environment.update();
        asyncTask.getValue().run();
        assertThat(movementComponent.path).isEqualTo(Polygon.ofNodes($(0, 0), $(100, 0)));
    }

    @Test
    void update_targetEntityFoundButNoPath_setsPathToNull(DefaultEnvironment environment, Async async, Loop loop, Navigation navigation) {
        when(loop.time()).thenReturn(Time.now());
        when(navigation.findPath($(0, 0), $(100, 0))).thenReturn(Optional.empty());
        var movementComponent = new PathMovementComponent(100, 200);
        environment
            .addSystem(new PathMovementSystem())
            .addEntity(new Entity(12)
                .add(new TransformComponent($(100, 0))))
            .addEntity(new Entity(2)
                .add(new PhysicsComponent())
                .add(new TransformComponent($(0, 0)))
                .add(movementComponent, movement -> {
                    movement.targetId = 12;
                    movement.path = Polygon.ofNodes($(0, 0), $(100, 0));
                })
            );

        ArgumentCaptor<Runnable> asyncTask = ArgumentCaptor.forClass(Runnable.class);

        when(async.runExclusive(eq(movementComponent), asyncTask.capture())).thenReturn(async);

        environment.update();
        asyncTask.getValue().run();
        assertThat(movementComponent.path).isNull();
    }
}
