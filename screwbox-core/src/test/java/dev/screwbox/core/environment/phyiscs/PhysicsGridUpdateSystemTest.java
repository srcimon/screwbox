package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.Grid;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsGridConfigurationComponent;
import dev.screwbox.core.environment.physics.PhysicsGridObstacleComponent;
import dev.screwbox.core.environment.physics.PhysicsGridUpdateSystem;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.physics.Physics;
import dev.screwbox.core.test.EnvironmentExtension;
import dev.screwbox.core.utils.Scheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Duration.ofMillis;
import static dev.screwbox.core.Time.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class PhysicsGridUpdateSystemTest {

    @Test
    void update_noConfiguration_doesntUpdateGrid(DefaultEnvironment environment, Physics physics) {
        environment.addSystem(new PhysicsGridUpdateSystem());

        environment.update();

        verify(physics, never()).setGrid(any());
    }

    @Test
    void update_gridOutdated_updatesPathfindingGrid(DefaultEnvironment environment, Physics physics, Loop loop) {
        when(loop.time()).thenReturn(now());

        var wall = new Entity()
                .add(new TransformComponent($$(0, 0, 100, 100)))
                .add(new PhysicsGridObstacleComponent());

        var air = new Entity()
                .add(new TransformComponent($$(-100, -100, 100, 100)));

        environment.addSystem(new PhysicsGridUpdateSystem())
                .addEntity(wall)
                .addEntity(air)
                .addEntity(new PhysicsGridConfigurationComponent($$(-100, -100, 200, 200), 100, Scheduler.withInterval(ofMillis(200))));

        environment.update();

        var gridCaptor = ArgumentCaptor.forClass(Grid.class);
        verify(physics).setGrid(gridCaptor.capture());

        Grid grid = gridCaptor.getValue();
        assertThat(grid.isFree(0, 0)).isTrue();
        assertThat(grid.isFree(0, 1)).isTrue();
        assertThat(grid.isFree(1, 0)).isTrue();
        assertThat(grid.isFree(1, 1)).isFalse();
    }

    @Test
    void update_invalidGridSize_throwsException(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(now());

        environment.addSystem(new PhysicsGridUpdateSystem())
                .addEntity(new PhysicsGridConfigurationComponent($$(-100, -100, 200, 200), 16, Scheduler.withInterval(ofMillis(200))));

        assertThatThrownBy(environment::update)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("area origin x should be dividable by grid size.");
    }
}
