package io.github.simonbas.screwbox.examples.platformer.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.components.CameraComponent;
import io.github.simonbas.screwbox.core.entities.internal.DefaultEntities;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerMarkerComponent;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GameSceneTest {

    @Mock
    Engine engine;

    @ParameterizedTest
    @ValueSource(strings = { "maps/0-1_intro.json", "maps/1-1_teufelsinsel.json", "maps/1-2_misty_caves.json" })
    void allMapsCanBeConvertetToEntities(String mapName) {
        Entities entities = new DefaultEntities(engine);

        new GameScene(mapName).importEntities(entities);

        assertThat(entities.allEntities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(CameraComponent.class))
                .anyMatch(e -> e.hasComponent(PlayerMarkerComponent.class));
    }
}
