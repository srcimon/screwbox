package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.async.Async;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Time.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class NavigationSystemTest {

    @Test
    void update_noRegion_noUpdate(DefaultEnvironment environment, Async async) {
        environment.addSystem(new NavigationSystem());

        environment.update();

        verify(async, never()).run(any(), any());
    }

    @Test
    void update_gridOutdated_updatesPathfindingGrid(DefaultEnvironment environment, Loop loop, Async async) {
        when(async.hasNoActiveTask(NavigationSystem.class)).thenReturn(true);
        when(loop.time()).thenReturn(now());

        createPathfindingSetup(environment);

        environment.update();

        verify(async).run(eq(NavigationSystem.class), any());
    }

    @Test
    void update_alreadyHasTask_noUpdate(DefaultEnvironment environment, Loop loop, Async async) {
        when(loop.time()).thenReturn(now());

        createPathfindingSetup(environment);

        environment.update();

        verify(async, never()).run(any(), any());
    }

    private void createPathfindingSetup(final DefaultEnvironment environment) {
        var wall = new Entity()
                .bounds($$(0, 0, 100, 100))
                .add(new ObstacleComponent());

        var air = new Entity()
                .bounds($$(-100, -100, 100, 100));

        var navigationRegion = new Entity()
                .bounds($$(-100, -100, 200, 200))
                .add(new NavigationRegionComponent());

        environment
                .addSystem(new NavigationSystem())
                .addEntity(wall)
                .addEntity(air)
                .addEntity(navigationRegion);
    }
}
