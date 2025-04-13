package dev.screwbox.platformer.pathfinding.scenes;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.platformer.pathfinding.components.PlayerMovementComponent;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class DemoSceneTest {

    @Mock
    Engine engine;

    @Test
    void testMapCanBeLoaded() {
        Environment environment = new DefaultEnvironment(engine);

        new DemoScene().populate(environment);

        assertThat(environment.entities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(PlayerMovementComponent.class));
    }
}
