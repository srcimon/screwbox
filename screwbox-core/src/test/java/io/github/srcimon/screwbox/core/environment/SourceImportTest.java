package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(EnvironmentExtension.class)
class SourceImportTest {

    @Test
    void as_converterNull_throwsException(DefaultEnvironment environment) {
        SourceImport<String> sourceImport = environment.importSource("Source of Inspiration");

        assertThatThrownBy(() -> sourceImport.as(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Converter must not be null");
    }

    @Test
    void usingIndex_indexFunctionIsNull_throwsException(DefaultEnvironment environment) {
        var sourceImport = environment.importSource("Source of Inspiration");

        assertThatThrownBy(() -> sourceImport
                .usingIndex(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Index function must not be null");
    }

    @Test
    void when_indexIsNull_throwsException(DefaultEnvironment environment) {
        var sourceImport = environment.importSource("Source of Inspiration")
                .usingIndex(String::length);

        assertThatThrownBy(() -> sourceImport.when(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Index must not be null");
    }

    @Test
    void as_inputWithoutCondition_createsEntity(DefaultEnvironment environment) {
        environment.importSource("Source of Inspiration")
                .as(source -> new Entity());

        assertThat(environment.entities()).hasSize(1);
    }

    @Test
    void as_conditionNotMet_noEntityCreated(DefaultEnvironment environment) {
        environment.importSource("Source of Inspiration")
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(environment.entities()).isEmpty();
    }

    @Test
    void ias_conditionMet_entityCreated(DefaultEnvironment environment) {
        environment.importSource("")
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(environment.entities()).hasSize(1);
    }

    @Test
    void as_multipleConditionsMet_multipleEntitiesCreated(DefaultEnvironment environment) {
        environment.importSource("")
                .when(String::isEmpty).as(source -> new Entity())
                .when(String::isEmpty).as(source -> new Entity());

        assertThat(environment.entities()).hasSize(2);
    }

    @Test
    void as_multipleSources_multipleEntitiesCreated(DefaultEnvironment environment) {
        environment.importSource(List.of("first", "second", "third"))
                .when(i -> i.contains("ir")).as(source -> new Entity());

        assertThat(environment.entities()).hasSize(2);
    }

    @Test
    void usingIndex_multipleSources_multipleEntitiesCreated(DefaultEnvironment environment) {
        environment.importSource(List.of("first", "second", "third"))
                .usingIndex(String::length)
                .when(6).as(source -> new Entity(6));

        assertThat(environment.entities()).hasSize(1)
                .allMatch(e -> e.id().get() == 6);
    }

    @Test
    void stopUsingIndex_returnsToDirectConversion(DefaultEnvironment environment) {
        environment.importSource(List.of("first", "second", "third"))
                .usingIndex(String::length)
                .when(6).as(source -> new Entity())
                .stopUsingIndex()
                .as(source -> new Entity());

        assertThat(environment.entities()).hasSize(4);
    }
}
