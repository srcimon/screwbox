package io.github.srcimon.screwbox.examples.pathfinding.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Ecosphere;
import io.github.srcimon.screwbox.core.ecosphere.components.WorldBoundsComponent;
import io.github.srcimon.screwbox.core.ecosphere.internal.DefaultEcosphere;
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
        Ecosphere ecosphere = new DefaultEcosphere(engine);

        new DemoScene().populate(ecosphere);

        assertThat(ecosphere.allEntities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(PlayerMovementComponent.class))
                .anyMatch(e -> e.hasComponent(WorldBoundsComponent.class));
    }
}
