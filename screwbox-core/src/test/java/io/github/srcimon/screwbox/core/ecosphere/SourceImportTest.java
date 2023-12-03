package io.github.srcimon.screwbox.core.ecosphere;

import io.github.srcimon.screwbox.core.ecosphere.internal.DefaultEcosphere;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(EntitiesExtension.class)
class SourceImportTest {

    @Test
    void as_converterNull_throwsException(DefaultEcosphere entities) {
        SourceImport<String> sourceImport = entities.importSource("Source of Inspiration");

        assertThatThrownBy(() -> sourceImport.as(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Converter must not be null");
    }

    @Test
    void usingIndex_indexFunctionIsNull_throwsException(DefaultEcosphere entities) {
        var sourceImport = entities.importSource("Source of Inspiration");

        assertThatThrownBy(() -> sourceImport
                .usingIndex(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Index function must not be null");
    }

    @Test
    void when_indexIsNull_throwsException(DefaultEcosphere entities) {
        var sourceImport = entities.importSource("Source of Inspiration")
                .usingIndex(String::length);

        assertThatThrownBy(() -> sourceImport.when(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Index must not be null");
    }

    @Test
    void as_inputWithoutCondition_createsEntity(DefaultEcosphere entities) {
        entities.importSource("Source of Inspiration")
                .as(source -> new Entity());

        assertThat(entities.allEntities()).hasSize(1);
    }

    @Test
    void as_conditionNotMet_noEntityCreated(DefaultEcosphere entities) {
        entities.importSource("Source of Inspiration")
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(entities.allEntities()).isEmpty();
    }

    @Test
    void ias_conditionMet_entityCreated(DefaultEcosphere entities) {
        entities.importSource("")
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(entities.allEntities()).hasSize(1);
    }

    @Test
    void as_multipleConditionsMet_multipleEntitiesCreated(DefaultEcosphere entities) {
        entities.importSource("")
                .when(String::isEmpty).as(source -> new Entity())
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(entities.allEntities()).hasSize(2);
    }

    @Test
    void as_multipleSources_multipleEntitiesCreated(DefaultEcosphere entities) {
        entities.importSource(List.of("first", "second", "third"))
                .when(i -> i.contains("ir")).as(source -> new Entity());

        assertThat(entities.allEntities()).hasSize(2);
    }

    @Test
    void usingIndex_multipleSources_multipleEntitiesCreated(DefaultEcosphere entities) {
        entities.importSource(List.of("first", "second", "third"))
                .usingIndex(String::length)
                .when(6).as(source -> new Entity(6));

        assertThat(entities.allEntities()).hasSize(1)
                .allMatch(e -> e.id().get() == 6);
    }

    @Test
    void stopUsingIndex_returnsToDirectConversion(DefaultEcosphere entities) {
        entities.importSource(List.of("first", "second", "third"))
                .usingIndex(String::length)
                .when(6).as(source -> new Entity())
                .stopUsingIndex()
                .as(source -> new Entity());

        assertThat(entities.allEntities()).hasSize(4);
    }
}
