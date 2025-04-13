package dev.screwbox.core.utils;

import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ai.PathMovementSystem;
import dev.screwbox.core.test.EnvironmentExtension;
import dev.screwbox.core.test.TestUtil;
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
        var instances = Reflections.createInstancesFromPackage("dev.screwbox.core.environment.ai", EntitySystem.class);

        assertThat(instances).hasSize(5).anyMatch(instance -> instance.getClass().equals(PathMovementSystem.class));
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
        var classes = Reflections.findClassesInPackage("dev.screwbox.core.test");

        assertThat(classes).containsExactlyInAnyOrder(EnvironmentExtension.class, TestUtil.class);
    }
}
