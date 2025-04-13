package io.github.srcimon.screwbox.platformer.scenes;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class GameSceneTest {

    @Mock
    Engine engine;

    @ParameterizedTest
    @ValueSource(strings = {"maps/0-1_intro.json", "maps/1-1_teufelsinsel.json", "maps/1-2_misty_caves.json"})
    void allMapsCanBeConvertetToEntities(String mapName) {
        Environment environment = new DefaultEnvironment(engine);

        new GameScene(mapName).importEntities(environment);

        assertThat(environment.entities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(CameraBoundsComponent.class))
                .anyMatch(e -> e.hasComponent(PlayerMarkerComponent.class));
    }
}
