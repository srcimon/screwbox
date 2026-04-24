package dev.screwbox.core.environment.importing;

import dev.screwbox.core.environment.Entity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImportOptionsTest {

    @Test
    void source_sourceNotNull_createsRulesetWithoutIndex() {
        var options = ImportOptions.source("test");

        assertThat(options.sources()).containsExactly("test");
        assertThat(options.hasIndex()).isFalse();
    }

    @Test
    void source_sourceNull_throwsException() {
        assertThatThrownBy(() -> ImportOptions.source(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("source must not be null");
    }

    @Test
    void sources_sourcesNull_throwsException() {
        assertThatThrownBy(() -> ImportOptions.sources(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("sources must not be null");
    }

    @Test
    void sources_sourcesNotNull_createsRulesetWithoutIndex() {
        var options = ImportOptions.sources(List.of(1, 2, 3));

        assertThat(options.sources()).containsExactly(1, 2, 3).isUnmodifiable();
        assertThat(options.hasIndex()).isFalse();
    }

    @Test
    void repeatAssignment_noLastAssignment_throwsException() {
        var options = ImportOptions.sources(List.of(1, 2, 3));

        assertThatThrownBy(() -> options.repeatLastAssignment(2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("cannot repeat assigment without specifying an assignment");
    }

    @Test
    void repeatLastAssignment_negativeCount_throwsException() {
        var options = ImportOptions.sources(List.of(1, 2, 3));
        options.assign(1, source -> new Entity());

        assertThatThrownBy(() -> options.repeatLastAssignment(-2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("repetition count must be positive (actual value: -2)");
    }

    @Test
    void repeatLastAssignment_positiveCount_repeatsAssignment() {
        var options = ImportOptions.sources(List.of(1, 2, 3))
            .assign(1, source -> new Entity(source).name("first"))
            .assign(2, source -> new Entity(source).name("second"))
            .repeatLastAssignment(10);

        assertThat(options.assignments()).hasSize(12);
    }
}
