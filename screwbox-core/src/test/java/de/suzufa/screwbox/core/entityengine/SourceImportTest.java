package de.suzufa.screwbox.core.entityengine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.test.extensions.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class SourceImportTest {

    @Test
    void as_converterNull_throwsException(DefaultEntityEngine entityEngine) {
        SourceImport<String> sourceImport = entityEngine.importSource("Source of Inspiration");

        assertThatThrownBy(() -> sourceImport.as(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Converter must not be null");
    }

    @Test
    void usingIndex_indexFunctionIsNull_throwsException(DefaultEntityEngine entityEngine) {
        var sourceImport = entityEngine.importSource("Source of Inspiration");

        assertThatThrownBy(() -> sourceImport
                .usingIndex(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Index function must not be null");
    }

    @Test
    void when_indexIsNull_throwsException(DefaultEntityEngine entityEngine) {
        var sourceImport = entityEngine.importSource("Source of Inspiration")
                .usingIndex(String::length);

        assertThatThrownBy(() -> sourceImport.when(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Index must not be null");
    }

    @Test
    void as_inputWithoutCondition_createsEntity(DefaultEntityEngine entityEngine) {
        entityEngine.importSource("Source of Inspiration")
                .as(source -> new Entity());

        assertThat(entityEngine.allEntities()).hasSize(1);
    }

    @Test
    void as_conditionNotMet_noEntityCreated(DefaultEntityEngine entityEngine) {
        entityEngine.importSource("Source of Inspiration")
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(entityEngine.allEntities()).isEmpty();
    }

    @Test
    void ias_conditionMet_entityCreated(DefaultEntityEngine entityEngine) {
        entityEngine.importSource("")
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(entityEngine.allEntities()).hasSize(1);
    }

    @Test
    void as_multipleConditionsMet_multipleEntitiesCreated(DefaultEntityEngine entityEngine) {
        entityEngine.importSource("")
                .when(String::isEmpty).as(source -> new Entity())
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(entityEngine.allEntities()).hasSize(2);
    }

    @Test
    void as_multipleSources_multipleEntitiesCreated(DefaultEntityEngine entityEngine) {
        entityEngine.importSource(List.of("first", "second", "third"))
                .when(i -> i.contains("ir")).as(source -> new Entity());

        assertThat(entityEngine.allEntities()).hasSize(2);
    }

    @Test
    void usingIndex_multipleSources_multipleEntitiesCreated(DefaultEntityEngine entityEngine) {
        entityEngine.importSource(List.of("first", "second", "third"))
                .usingIndex(String::length)
                .when(6).as(source -> new Entity(6));

        assertThat(entityEngine.allEntities()).hasSize(1)
                .allMatch(e -> e.id().get() == 6);
    }

    @Test
    void stopUsingIndex_returnsToDirectConversion(DefaultEntityEngine entityEngine) {
        entityEngine.importSource(List.of("first", "second", "third"))
                .usingIndex(String::length)
                .when(6).as(source -> new Entity())
                .stopUsingIndex()
                .as(source -> new Entity());

        assertThat(entityEngine.allEntities()).hasSize(4);
    }
}
