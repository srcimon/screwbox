package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Reflections;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;

class DocumentationTest {

    @ParameterizedTest
    @MethodSource("allComponentClasses")
    void verifyComponentIsListedInComponentsOverview(Class<? extends Component> componentClazz) {
        assertThat(getDocsContent("reference/components-overview.md")).contains(componentClazz.getSimpleName());
    }

    private static Stream<Arguments> allComponentClasses() {
        return Reflections.findClassesInPackage("io.github.srcimon.screwbox.core.environment").stream()
                .filter(Component.class::isAssignableFrom)
                .filter(not(Class::isMemberClass))
                .map(Arguments::of);
    }

    static String getDocsContent(final String path) {
        try {
            return Files.readString(Path.of("../docs/docs/" + path));
        } catch (IOException e) {
            throw new IllegalStateException("error reading documentation file: " + path, e);
        }
    }
}
