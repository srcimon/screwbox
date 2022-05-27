package de.suzufa.screwbox.playground.debo.scenes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.CameraComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

class GameSceneTest {

    @ParameterizedTest
    @ValueSource(strings = { "maps/0-1_intro.json", "maps/1-1_teufelsinsel.json", "maps/1-2_misty_caves.json" })
    void allMapsCanBeConvertetToEntities(String mapName) {
        List<Entity> entities = new GameScene(mapName).createEntitiesFromMap();

        assertThat(entities).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(CameraComponent.class))
                .anyMatch(e -> e.hasComponent(PlayerMarkerComponent.class));
    }
}
