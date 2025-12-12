package dev.screwbox.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isStatic;
import static org.assertj.core.api.Assertions.assertThat;

public class ArchitectureTest {

    @ParameterizedTest
    @MethodSource("dev.screwbox.core.test.TestSources#allComponentClasses")
    void testAllComponentsHaveSerialVersionUID(Class<?> componentClazz) {
        assertThat(componentClazz.getDeclaredFields())
                .filteredOn(field -> isStatic(field.getModifiers()))
                .filteredOn(field -> isPrivate(field.getModifiers()))
                .anyMatch(field -> field.getName().equals("serialVersionUID"));
    }
}
