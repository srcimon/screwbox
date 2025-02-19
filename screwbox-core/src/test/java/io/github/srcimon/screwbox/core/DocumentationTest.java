package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Reflections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

class DocumentationTest {

    //TODO move to docs?

    @ParameterizedTest
    @MethodSource("allComponentNames")
    void verifyAllComponentsAreDocumentedWithinComponentOverview(String componentName) {
        //TODO
        try {
           final var allLines = Files.readAllLines(Path.of("../docs/docs/reference/components-overview.md"));
            Assertions.assertThat(allLines).anyMatch(l -> l.contains(componentName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Arguments> allComponentNames() {
        return Reflections.findClassesInPackage("io.github.srcimon.screwbox.core.environment").stream()
                .filter(Component.class::isAssignableFrom)
                .map(Class::getSimpleName)
                .map(Arguments::of);



    //TODO find implementations in package...

    }
}
