package de.suzufa.screwbox.examples.platformer.scenes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.components.CameraComponent;
import de.suzufa.screwbox.core.entities.systems.StopEngineOnMaxFramesSystem;
import de.suzufa.screwbox.examples.platformer.components.PlayerMarkerComponent;

class GameSceneTest {

    @ParameterizedTest
    @ValueSource(strings = { "maps/0-1_intro.json", "maps/1-1_teufelsinsel.json", "maps/1-2_misty_caves.json" })
    void allMapsCanBeConvertetToEntities(String mapName) {
        var engine = ScrewBox.createHeadlessEngine();

        engine.scenes().add(new GameScene(mapName));
        Entities entities = engine.scenes().entitiesOf(GameScene.class);
        entities.add(new StopEngineOnMaxFramesSystem(1));
        engine.start(GameScene.class);

        assertThat(entities.allEntities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(CameraComponent.class))
                .anyMatch(e -> e.hasComponent(PlayerMarkerComponent.class));
    }
}
