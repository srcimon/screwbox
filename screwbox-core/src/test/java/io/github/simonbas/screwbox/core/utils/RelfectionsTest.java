package io.github.simonbas.screwbox.core.utils;

import io.github.simonbas.screwbox.core.test.EntitiesExtension;
import io.github.simonbas.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        var classes = Reflections.findClassesInPackage("io.github.simonbas.screwbox.core.test");

        assertThat(classes).containsExactlyInAnyOrder(EntitiesExtension.class, TestUtil.class);
    }
}
