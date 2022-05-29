package de.suzufa.screwbox.core.resources;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

class EntityLoaderTest {

    EntityLoader<String> entityLoader;

    @BeforeEach
    void beforeEach() {
        entityLoader = new EntityLoader<>();
    }

    @Test
    void createEntitiesFrom_noExtractors_empty() {
        List<Entity> entities = entityLoader
                .add(boxConverter(), String.class)
                .add(playerConverter(), String.class)
                .createEnttiesFrom("There are\ntwo boxes,\nBox\nBox and a Player.");

        assertThat(entities).isEmpty();
    }

    @Test
    void createEntitiesFrom_extractorsAndConverters_returnsEntities() {
        Extractor<String, String> lineExtractor = input -> input.lines().toList();
        Extractor<String, String> wordExtractor = input -> List.of(input.split(" "));

        List<Entity> entities = entityLoader
                .add(List.of(lineExtractor, wordExtractor))
                .add(boxConverter(), String.class)
                .add(playerConverter(), String.class)
                .createEnttiesFrom("There are\ntwo boxes,\nBox\nBox and a Player.");

        assertThat(entities).hasSize(3)
                .allMatch(e -> e.hasComponent(TransformComponent.class))
                .anyMatch(e -> e.hasComponent(ColliderComponent.class));
    }

    private EntityConverter<String> boxConverter() {
        EntityConverter<String> boxConverter = new EntityConverter<String>() {

            @Override
            public boolean accepts(String object) {
                return object.startsWith("Box");
            }

            @Override
            public Entity convert(String object) {
                return new Entity().add(new TransformComponent(Bounds.max()));
            }
        };
        return boxConverter;
    }

    private EntityConverter<String> playerConverter() {
        EntityConverter<String> playerConverter = new EntityConverter<String>() {

            @Override
            public boolean accepts(String object) {
                return object.startsWith("Player");
            }

            @Override
            public Entity convert(String object) {
                return new Entity()
                        .add(new TransformComponent(Bounds.max()))
                        .add(new ColliderComponent());
            }
        };
        return playerConverter;
    }
}
