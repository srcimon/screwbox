package de.suzufa.screwbox.examples.pathfinding.scenes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.examples.pathfinding.components.PlayerMovementComponent;

@ExtendWith(MockitoExtension.class)
class DemoSceneTest {

    @Mock
    Engine engine;

    @Test
    void testMapCanBeLoaded() {
        Entities entities = new DefaultEntities(engine);

        new DemoScene("map.json").initialize(entities);

        assertThat(entities.allEntities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(PlayerMovementComponent.class))
                .anyMatch(e -> e.hasComponent(WorldBoundsComponent.class));
    }
}
