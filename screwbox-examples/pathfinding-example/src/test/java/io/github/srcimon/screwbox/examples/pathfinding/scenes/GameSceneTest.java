package io.github.srcimon.screwbox.examples.pathfinding.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.components.WorldBoundsComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.examples.pathfinding.components.PlayerMovementComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DemoSceneTest {

    @Mock
    Engine engine;

    @Test
    void testMapCanBeLoaded() {
        Environment environment = new DefaultEnvironment(engine);

        new DemoScene().populate(environment);

        assertThat(environment.entities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(PlayerMovementComponent.class))
                .anyMatch(e -> e.hasComponent(WorldBoundsComponent.class));
    }
}
