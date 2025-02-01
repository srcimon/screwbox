package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.ai.PathMovementSystem;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReflectionsTest {

    @Test
    void createInstancesFromPackage_packageNameNull_throwsException() {
        assertThatThrownBy(() -> Reflections.createInstancesFromPackage(null, EntitySystem.class))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("packageName must not be null");
    }

    @Test
    void createInstancesFromPackage_packageContainsClassesWithDefaultConstructor_returnsInstances() {
        var instances = Reflections.createInstancesFromPackage("io.github.srcimon.screwbox.core.environment.ai", EntitySystem.class);

        assertThat(instances).hasSize(4).anyMatch(instance -> instance.getClass().equals(PathMovementSystem.class));
    }

    @Test
    void findClassesInPackage_packageDoesntExist_emptyList() {
        var classes = Reflections.findClassesInPackage("de.unknown");

        assertThat(classes).isEmpty();
    }

    @Test
    void findClassesInPackage_packageNull_throwsException() {
        assertThatThrownBy(() -> Reflections.findClassesInPackage(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("packageName must not be null");
    }

    @Test
    void findClassesInPackage_packageExists_findsClasses() {
        var classes = Reflections.findClassesInPackage("io.github.srcimon.screwbox.core.test");

        assertThat(classes).containsExactlyInAnyOrder(EnvironmentExtension.class, TestUtil.class);
    }
}
