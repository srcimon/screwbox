package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.PhysicsSystem;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.navigation.internal.DefaultNavigation;
import dev.screwbox.core.test.EnvironmentExtension;
import dev.screwbox.core.utils.TileMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.Time.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class PatrolMovementSystemTest {

    final List<Double> xPositions = new ArrayList<>();

    @BeforeEach
    void setUp(Engine engine, Loop loop, DefaultEnvironment environment) {
        when(engine.navigation()).thenReturn(new DefaultNavigation(engine)); // real raycasts
        when(loop.delta()).thenReturn(0.4);
        var map = TileMap.fromString("""
                   # P
                ########   ######
                """);
        environment
                .addSystem(new PatrolMovementSystem())
                .addSystem(new PhysicsSystem())
                .addSystem(x -> xPositions.add(x.environment().fetchById(0).position().x()))
                .importSourceDEPRECATED(map.tiles())
                .usingIndex(TileMap.Tile::value)
                .when('#').as(tile -> new Entity().name("ground")
                        .add(new ColliderComponent())
                        .add(new TransformComponent(tile.bounds())))
                .when('P').as(tile -> new Entity(0).name("patrol")
                        .add(new PatrolMovementComponent(10))
                        .add(new PhysicsComponent())
                        .add(new TransformComponent(tile.bounds())));
    }

    @Test
    void update_checksForRouteChange_stopsAtWallAndOnCliff(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(
                now(),
                now().addSeconds(1),
                now().addSeconds(2),
                now().addSeconds(3),
                now().addSeconds(4),
                now().addSeconds(5),
                now().addSeconds(6),
                now().addSeconds(7),
                now().addSeconds(8),
                now().addSeconds(9));

        environment.updateTimes(10);

        assertThat(xPositions).containsExactly(88.0, 84.0, 80.0, 76.0, 72.0, 76.0, 80.0, 84.0, 88.0, 92.0);
    }

    @Test
    void update_noCheckForRouteChange_doesntStopAtWallOrCliff(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(now());

        environment.updateTimes(10);

        assertThat(xPositions).containsExactly(88.0, 84.0, 80.0, 76.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0);
    }

    @AfterEach
    void tearDown() {
        xPositions.clear();
    }
}
