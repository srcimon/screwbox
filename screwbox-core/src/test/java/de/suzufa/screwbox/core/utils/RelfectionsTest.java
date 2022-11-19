package de.suzufa.screwbox.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.test.EntitiesExtension;
import de.suzufa.screwbox.core.test.TestUtil;

class RelfectionsTest {

    @Test
    void findClassesInPackage_packageDoesntExist_emptyList() {
        var classes = Reflections.findClassesInPackage("de.unknown");

        assertThat(classes).isEmpty();
    }

    @Test
    void findClassesInPackage_packageNull_exception() {
        assertThatThrownBy(() -> Reflections.findClassesInPackage(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("packageName must not be null");
    }

    @Test
    void findClassesInPackage_packageExists_findsClasses() {
        var classes = Reflections.findClassesInPackage("de.suzufa.screwbox.core.test");

        assertThat(classes).containsExactly(EntitiesExtension.class, TestUtil.class);
    }
}
