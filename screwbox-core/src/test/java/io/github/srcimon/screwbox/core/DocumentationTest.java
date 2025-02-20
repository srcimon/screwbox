package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.test.TestUtil;
import io.github.srcimon.screwbox.core.utils.Reflections;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentationTest {


    @ParameterizedTest
    @MethodSource("allComponentClasses")
    void verifyComponentIsListedInComponentsOverview(Class<? extends Component> componentClazz)  {
        //TODO
        final var componentsOverview = TestUtil.getDocsContent("reference/components-overview.md");
        assertThat(componentsOverview).contains(componentClazz.getSimpleName());
    }

    private static Stream<Arguments> allComponentClasses() {
        return Reflections.findClassesInPackage("io.github.srcimon.screwbox.core.environment").stream()
                .filter(Component.class::isAssignableFrom)
                .map(Arguments::of);


        //TODO find implementations in package...

    }
}
