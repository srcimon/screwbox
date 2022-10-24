package de.suzufa.screwbox.playground.debo.scenes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.components.CameraComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.entities.internal.EntityManager;
import de.suzufa.screwbox.core.entities.internal.SystemManager;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

class GameSceneTest {

    @ParameterizedTest
    @ValueSource(strings = { "maps/0-1_intro.json", "maps/1-1_teufelsinsel.json", "maps/1-2_misty_caves.json" })
    void allMapsCanBeConvertetToEntities(String mapName) {
        var engine = Mockito.mock(Engine.class);
        var entityManager = new EntityManager();
        var systemManager = new SystemManager(engine, entityManager);
        Entities entities = new DefaultEntities(entityManager, systemManager);

        new GameScene(mapName).importEntities(entities);

        assertThat(entities.allEntities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(CameraComponent.class))
                .anyMatch(e -> e.hasComponent(PlayerMarkerComponent.class));
    }
}
