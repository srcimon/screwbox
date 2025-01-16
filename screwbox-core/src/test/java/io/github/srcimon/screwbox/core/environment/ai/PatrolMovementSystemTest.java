package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsSystem;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.physics.internal.DefaultPhysics;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static io.github.srcimon.screwbox.core.Time.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class PatrolMovementSystemTest {

    List<Double> xPositions = new ArrayList<>();

    @BeforeEach
    void setUp(Engine engine, Loop loop, DefaultEnvironment environment) {
        when(engine.physics()).thenReturn(new DefaultPhysics(engine)); // real raycasts
        when(loop.delta()).thenReturn(0.4);
        var map = AsciiMap.fromString("""
                   # P
                ########   ######
                """);
        environment
                .addSystem(new PatrolMovementSystem())
                .addSystem(new PhysicsSystem())
                .addSystem(x -> xPositions.add(x.environment().fetchById(0).position().x()))
                .importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
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

        assertThat(xPositions).containsExactly(88.0, 92.0, 96.0, 100.0, 104.0, 100.0, 96.0, 92.0, 88.0, 92.0);
    }

    @Test
    void update_noCheckForRouteChange_doesntStopAtWallOrCliff(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(now());

        environment.updateTimes(10);

        assertThat(xPositions).containsExactly(88.0, 92.0, 96.0, 100.0, 104.0, 108.0, 112.0, 116.0, 120.0, 124.0);
    }

    @AfterEach
    void tearDown() {
        xPositions.clear();
    }
}
