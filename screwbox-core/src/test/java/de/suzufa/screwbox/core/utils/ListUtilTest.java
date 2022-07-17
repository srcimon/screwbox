package de.suzufa.screwbox.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

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
}
