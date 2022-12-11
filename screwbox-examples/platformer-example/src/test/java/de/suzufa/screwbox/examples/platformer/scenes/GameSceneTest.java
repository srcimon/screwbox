package de.suzufa.screwbox.examples.platformer.scenes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.components.CameraComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.entities.internal.DefaultEntityManager;
import de.suzufa.screwbox.core.entities.internal.DefaultSystemManager;
import de.suzufa.screwbox.examples.platformer.components.PlayerMarkerComponent;

@ExtendWith(MockitoExtension.class)
class GameSceneTest {

    @Mock
    Engine engine;

    @ParameterizedTest
    @ValueSource(strings = { "maps/0-1_intro.json", "maps/1-1_teufelsinsel.json", "maps/1-2_misty_caves.json" })
    void allMapsCanBeConvertetToEntities(String mapName) {
        var entityManager = new DefaultEntityManager();
        var systemManager = new DefaultSystemManager(engine, entityManager);
        Entities entities = new DefaultEntities(entityManager, systemManager);

        new GameScene(mapName).importEntities(entities);

        assertThat(entities.allEntities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(CameraComponent.class))
                .anyMatch(e -> e.hasComponent(PlayerMarkerComponent.class));
    }
}
