package de.suzufa.screwbox.examples.pathfinding.scenes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.entities.internal.EntityManager;
import de.suzufa.screwbox.core.entities.internal.SystemManager;
import de.suzufa.screwbox.examples.pathfinding.components.PlayerMovementComponent;

@ExtendWith(MockitoExtension.class)
class DemoSceneTest {

    @ParameterizedTest
    @ValueSource(strings = { "maze/map.json" })
    void allMapsCanBeConvertetToEntities(String map) {
        var engine = Mockito.mock(Engine.class);
        var entityManager = new EntityManager();
        var systemManager = new SystemManager(engine, entityManager);
        Entities entities = new DefaultEntities(entityManager, systemManager);

        new DemoScene(map).importEntities(entities);

        assertThat(entities.allEntities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(PlayerMovementComponent.class))
                .anyMatch(e -> e.hasComponent(WorldBoundsComponent.class));
    }
}
