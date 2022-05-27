package de.suzufa.screwbox.playground.debo.scenes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.CameraComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.tiled.Map;
import de.suzufa.screwbox.tiled.TiledSupport;

class MapsTest {

    @ParameterizedTest
    @ValueSource(strings = { "0-1_intro", "1-1_teufelsinsel", "1-2_misty_caves" })
    void allMapsCanBeConvertetToEntities(String mapName) {
        Map map = TiledSupport.loadMap("maps/" + mapName + ".json");
        var mapConverter = new GameScene(mapName).gameConverter(map);
        List<Entity> entities = mapConverter.createEnttiesFrom(map);

        assertThat(entities).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(CameraComponent.class))
                .anyMatch(e -> e.hasComponent(PlayerMarkerComponent.class));
    }
}
