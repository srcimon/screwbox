package io.github.srcimon.screwbox.core.environment.systems;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.components.PathfindingBlockingComponent;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.environment.components.WorldBoundsComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.physics.Physics;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Time.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EntitiesExtension.class)
class PathfindingGridCreationSystemTest {

    @Test
    void update_noWorldBounds_throwsException(DefaultEnvironment entities, Loop loop) {
        when(loop.lastUpdate()).thenReturn(now());
        Sheduler sheduler = Sheduler.withInterval(Duration.ofMillis(200));
        entities.addSystem(new PathfindingGridCreationSystem(16, sheduler));

        assertThatThrownBy(() -> entities.update())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("didn't find exactly one entity matching Archetype");
    }

    @Test
    void update_noGridPresent_updatesPathfindingGrid(DefaultEnvironment entities, Physics physics, Loop loop) {
        when(loop.lastUpdate()).thenReturn(now());
        Sheduler sheduler = Sheduler.withInterval(Duration.ofMillis(200));
        var worldBounds = new Entity()
                .add(new WorldBoundsComponent())
                .add(new TransformComponent($$(-100, -100, 200, 200)));

        var wall = new Entity()
                .add(new TransformComponent($$(0, 0, 100, 100)))
                .add(new PathfindingBlockingComponent());

        var air = new Entity()
                .add(new TransformComponent($$(-100, -100, 100, 100)));

        entities.addSystem(new PathfindingGridCreationSystem(100, sheduler))
                .addEntity(wall)
                .addEntity(air)
                .addEntity(worldBounds);

        entities.update();

        var gridCaptor = ArgumentCaptor.forClass(Grid.class);
        verify(physics).setGrid(gridCaptor.capture());

        Grid grid = gridCaptor.getValue();
        assertThat(grid.isFree(0, 0)).isTrue();
        assertThat(grid.isFree(0, 1)).isTrue();
        assertThat(grid.isFree(1, 0)).isTrue();
        assertThat(grid.isFree(1, 1)).isFalse();
    }

    @Test
    void update_invalidGridSize_throwsException(DefaultEnvironment entities, Loop loop) {
        when(loop.lastUpdate()).thenReturn(now());
        Sheduler sheduler = Sheduler.withInterval(Duration.ofMillis(200));
        var worldBounds = new Entity()
                .add(new WorldBoundsComponent())
                .add(new TransformComponent($$(-100, -100, 200, 200)));

        entities.addSystem(new PathfindingGridCreationSystem(16, sheduler)).addEntity(worldBounds);

        assertThatThrownBy(() -> entities.update())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("area origin x should be dividable by grid size.");
    }
}
