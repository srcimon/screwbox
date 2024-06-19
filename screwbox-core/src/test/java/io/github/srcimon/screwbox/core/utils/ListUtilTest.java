package io.github.srcimon.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class ListUtilTest {

    @Test
    void merge_listContainsDuplicates_removesDuplicates() {
        var male = List.of("Andreas", "Andrea");
        var female = List.of("Kathrin", "Andrea");

        var merged = ListUtil.merge(male, female);

        assertThat(merged)
                .contains("Andreas", "Andrea", "Kathrin")
                .hasSize(3);
    }

    @Test
    void merge_firstIsEmpty_returnsSecond() {
        var male = List.of("Andreas", "Andrea");

        var merged = ListUtil.merge(emptyList(), male);

        assertThat(merged).isEqualTo(male);
    }

    @Test
    void merge_secondIsEmpty_returnsSecond() {
        var female = List.of("Kathrin", "Andrea");

        var merged = ListUtil.merge(female, emptyList());

        assertThat(merged).isEqualTo(female);
    }

    @Test
    void randomFrom_listIsEmpty_returnsNull() {
        var strings = new ArrayList<>();

        var result = ListUtil.randomFrom(strings);

        assertThat(result).isNull();
    }

    @Test
    void randomFrom_listHasEntries_returnsEntry() {
        var strings = List.of("one", "two", "three");

        var result = ListUtil.randomFrom(strings);

        assertThat(result).isIn("one", "two", "three");
    }

    @Test
    void randomFrom_multipleItemsGiven_returnsEntry() {
        var result = ListUtil.randomFrom("one", "two", "three");

        assertThat(result).isIn("one", "two", "three");
    }

    @Test
    void emptyWhenNull_nonNull_returnsOriginal() {
        var result = ListUtil.emptyWhenNull(List.of("A", "B"));

        assertThat(result).isEqualTo(List.of("A", "B"));
    }

    @Test
    void emptyWhenNull_null_empty() {
        var result = ListUtil.emptyWhenNull((List<String>) null);

        assertThat(result).isEmpty();
    }

    @Test
    void containsDuplicates_noDuplicates_isFalse() {
        boolean containsDuplicates = ListUtil.containsDuplicates(List.of("A", "X", "a"));
        assertThat(containsDuplicates).isFalse();
    }

    @Test
    void containsDuplicates_duplicates_isTrue() {
        boolean containsDuplicates = ListUtil.containsDuplicates(List.of("A", "X", "a", "X"));
        assertThat(containsDuplicates).isTrue();
    }
}
