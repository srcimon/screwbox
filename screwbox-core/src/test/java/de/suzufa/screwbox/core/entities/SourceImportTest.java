package de.suzufa.screwbox.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.ScrewBox;

class SourceImportTest {

    private Entities entities;

    @BeforeEach
    void beforeEach() {
        entities = ScrewBox.createHeadlessEngine().entities();
    }

    @Test
    void as_converterNull_throwsException() {
        SourceImport<String> sourceImport = entities.importSource("Source of Inspiration");

        assertThatThrownBy(() -> sourceImport.as(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Converter must not be null");
    }

    @Test
    void usingIndex_indexFunctionIsNull_throwsException() {
        var sourceImport = entities.importSource("Source of Inspiration");

        assertThatThrownBy(() -> sourceImport
                .usingIndex(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Index function must not be null");
    }

    @Test
    void when_indexIsNull_throwsException() {
        var sourceImport = entities.importSource("Source of Inspiration")
                .usingIndex(String::length);

        assertThatThrownBy(() -> sourceImport.when(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Index must not be null");
    }

    @Test
    void as_inputWithoutCondition_createsEntity() {
        entities.importSource("Source of Inspiration")
                .as(source -> new Entity());

        assertThat(entities.allEntities()).hasSize(1);
    }

    @Test
    void as_conditionNotMet_noEntityCreated() {
        entities.importSource("Source of Inspiration")
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(entities.allEntities()).isEmpty();
    }

    @Test
    void ias_conditionMet_entityCreated() {
        entities.importSource("")
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(entities.allEntities()).hasSize(1);
    }

    @Test
    void as_multipleConditionsMet_multipleEntitiesCreated() {
        entities.importSource("")
                .when(String::isEmpty).as(source -> new Entity())
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(entities.allEntities()).hasSize(2);
    }

    @Test
    void as_multipleSources_multipleEntitiesCreated() {
        entities.importSource(List.of("first", "second", "third"))
                .when(i -> i.contains("ir")).as(source -> new Entity());

        assertThat(entities.allEntities()).hasSize(2);
    }

    @Test
    void usingIndex_multipleSources_multipleEntitiesCreated() {
        entities.importSource(List.of("first", "second", "third"))
                .usingIndex(String::length)
                .when(6).as(source -> new Entity(6));

        assertThat(entities.allEntities()).hasSize(1)
                .allMatch(e -> e.id().get() == 6);
    }

    @Test
    void stopUsingIndex_returnsToDirectConversion() {
        entities.importSource(List.of("first", "second", "third"))
                .usingIndex(String::length)
                .when(6).as(source -> new Entity())
                .stopUsingIndex()
                .as(source -> new Entity());

        assertThat(entities.allEntities()).hasSize(4);
    }
}
