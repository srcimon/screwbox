package dev.screwbox.core.environment.imports;

import dev.screwbox.core.environment.importing.ImportCondition;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImportConditionTest {

    @Test
    void testIndex() {
        var condition = ImportCondition.index("i");

        assertThat(condition.test("source", "i", null)).isTrue();
        assertThat(condition.test("source", "2", null)).isFalse();
    }

    @Test
    void testNoneOf() {
        var condition = ImportCondition.noneOf(ImportCondition.index('X'), ImportCondition.index('Y'));

        assertThat(condition.test("source", 'i', null)).isTrue();
        assertThat(condition.test("source", 'Y', null)).isFalse();
    }

    @Test
    void testAllOf() {
        var condition = ImportCondition.allOf(ImportCondition.index('X'), ImportCondition.index('X'));

        assertThat(condition.test("source", 'X', null)).isTrue();
        assertThat(condition.test("source", 'x', null)).isFalse();
    }

    @Test
    void testAlways() {
        var condition = ImportCondition.always();

        assertThat(condition.test("source", 'X', null)).isTrue();
    }

    @Test
    void testProbability() {
        var condition = ImportCondition.probability(0.5);

        Set<Boolean> results = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            results.add(condition.test("source", 'X', null));
        }
        assertThat(results).contains(false, true);
    }

    @Test
    void probability_valueOutOfRange_throwsException() {
        assertThatThrownBy(() -> ImportCondition.probability(1.5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("probability must be between 0 and 1 (actual value: 1.5)");
    }
}
