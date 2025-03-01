package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.ShaderBundle;
import io.github.srcimon.screwbox.core.utils.Cache;
import io.github.srcimon.screwbox.core.utils.Reflections;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;

class DocumentationTest {

    private static final Cache<String, String> DOC_CONTENT_CACHE = new Cache<>();

    @ParameterizedTest
    @MethodSource("allComponentClasses")
    void verifyComponentIsListedInComponentsOverview(Class<? extends Component> componentClazz) {
        assertThat(getDocsContent("reference/components-overview.md")).contains(componentClazz.getSimpleName());
    }

    @ParameterizedTest
    @MethodSource("allAssetBundles")
    void verifyAssetBundleIsListedInAssetDoc(Class<? extends Component> assetBundleClazz) {
        assertThat(getDocsContent("core-modules/assets.md")).contains(assetBundleClazz.getSimpleName());
    }

    @ParameterizedTest
    @EnumSource(Ease.class)
    void verifyAllEaseValuesAreListedInOverview(Ease ease) {
        assertThat(getDocsContent("reference/ease/index.md")).contains(ease.name());
    }

    @ParameterizedTest
    @EnumSource(ShaderBundle.class)
    void verifyAllShadersAreListedInOverview(ShaderBundle shader) {
        assertThat(getDocsContent("reference/shaders/index.md")).contains(shader.name());
    }

    private static Stream<Arguments> allComponentClasses() {
        return Reflections.findClassesInPackage("io.github.srcimon.screwbox.core.environment").stream()
                .filter(Component.class::isAssignableFrom)
                .filter(not(Class::isMemberClass))
                .map(Arguments::of);
    }

    private static Stream<Arguments> allAssetBundles() {
        return Reflections.findClassesInPackage("io.github.srcimon.screwbox.core").stream()
                .filter(AssetBundle.class::isAssignableFrom)
                .filter(not(Class::isMemberClass))
                .map(Arguments::of);
    }

    static String getDocsContent(final String path) {
        return DOC_CONTENT_CACHE.getOrElse(path, () -> {
            try {
                return Files.readString(Path.of("../docs/docs/" + path));
            } catch (IOException e) {
                throw new IllegalStateException("error reading documentation file: " + path, e);
            }
        });
    }
}
