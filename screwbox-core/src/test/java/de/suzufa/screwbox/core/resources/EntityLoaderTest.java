package de.suzufa.screwbox.core.resources;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

//TODO: FIX ALL NAMES
class EntityLoaderTest {

    EntityExtractor<String> entityLoader;

    @BeforeEach
    void beforeEach() {
        entityLoader = EntityExtractor.from("There are\ntwo boxes,\nBox\nBox and a Player.");
    }

    @Test
    void buildAllEntities_noConverters_emptyList() {
        var result = entityLoader.buildAllEntities();

        assertThat(result).isEmpty();
    }

    @Test
    void buildAllEntities_extractorsAndConvertersPresent_returnEntities() {
        List<Entity> entities = entityLoader
                .apply(sentenceConverter())

                .forEach(input -> input.lines().toList())
                .use(boxConverter())
                .use(playerConverter())
                .endLoop()

                .forEach(input -> List.of(input.split(" ")))
                .use(boxConverter())
                .use(playerConverter())
                .endLoop()

                .buildAllEntities();

        assertThat(entities).hasSize(4)
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

    private EntityConverter<String> sentenceConverter() {
        EntityConverter<String> boxConverter = new EntityConverter<String>() {

            @Override
            public boolean accepts(String object) {
                return true;
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
