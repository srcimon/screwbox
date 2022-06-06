package de.suzufa.screwbox.playground.debo.scenes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.components.CameraComponent;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityManager;
import de.suzufa.screwbox.core.entityengine.internal.DefaultSystemManager;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

class GameSceneTest {

    @ParameterizedTest
    @ValueSource(strings = { "maps/0-1_intro.json", "maps/1-1_teufelsinsel.json", "maps/1-2_misty_caves.json" })
    void allMapsCanBeConvertetToEntities(String mapName) {
        var engine = Mockito.mock(Engine.class);
        var entityManager = new DefaultEntityManager();
        var systemManager = new DefaultSystemManager(engine, entityManager);
        EntityEngine entityEngine = new DefaultEntityEngine(entityManager, systemManager);

        new GameScene(mapName).addMapEntities(entityEngine);

        assertThat(entityEngine.allEntities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(CameraComponent.class))
                .anyMatch(e -> e.hasComponent(PlayerMarkerComponent.class));
    }
}
