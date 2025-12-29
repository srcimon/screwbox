package dev.screwbox.core.environment.imports;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.ImportRuleset;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImportRulesetTest {

    @Test
    void source_sourceNotNull_createsRulesetWithoutIndex() {
        var ruleset = ImportRuleset.source("test");

        assertThat(ruleset.sources()).containsExactly("test");
        assertThat(ruleset.hasIndex()).isFalse();
    }

    @Test
    void source_sourceNull_throwsException() {
        assertThatThrownBy(() -> ImportRuleset.source(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("source must not be null");
    }

    @Test
    void sources_sourcesNull_throwsException() {
        assertThatThrownBy(() -> ImportRuleset.sources(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("sources must not be null");
    }

    @Test
    void sources_sourcesNotNull_createsRulesetWithoutIndex() {
        var ruleset = ImportRuleset.sources(List.of(1, 2, 3));

        assertThat(ruleset.sources()).containsExactly(1, 2, 3).isUnmodifiable();
        assertThat(ruleset.hasIndex()).isFalse();
    }

}
