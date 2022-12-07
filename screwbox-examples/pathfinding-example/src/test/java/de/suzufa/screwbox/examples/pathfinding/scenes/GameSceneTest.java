package de.suzufa.screwbox.examples.pathfinding.scenes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.entities.systems.StopEngineOnMaxFramesSystem;
import de.suzufa.screwbox.examples.pathfinding.components.PlayerMovementComponent;

class DemoSceneTest {

    @Test
    void testMapCanBeLoaded() {
        var engine = ScrewBox.createHeadlessEngine();

        engine.scenes().add(new DemoScene("map.json"));
        Entities entities = engine.scenes().entitiesOf(DemoScene.class);
        entities.add(new StopEngineOnMaxFramesSystem(1));
        engine.start(DemoScene.class);

        assertThat(entities.allEntities()).hasSizeGreaterThan(50)
                .anyMatch(e -> e.hasComponent(PlayerMovementComponent.class))
                .anyMatch(e -> e.hasComponent(WorldBoundsComponent.class));
    }
}
