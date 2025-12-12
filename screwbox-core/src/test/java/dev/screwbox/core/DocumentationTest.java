package dev.screwbox.core;

import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.utils.Cache;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentationTest {

    private static final Cache<String, String> DOC_CONTENT_CACHE = new Cache<>();

    @ParameterizedTest
    @MethodSource("dev.screwbox.core.test.TestSources#allComponentClasses")
    void verifyComponentIsListedInComponentsOverview(Class<?> componentClazz) {
        assertThat(getDocsContent("reference/components-overview.md")).contains(componentClazz.getSimpleName());
    }

    @ParameterizedTest
    @MethodSource("dev.screwbox.core.test.TestSources#allAssetBundles")
    void verifyAssetBundleIsListedInAssetDoc(Class<?> assetBundleClazz) {
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

    @ParameterizedTest
    @MethodSource("dev.screwbox.core.test.TestSources#allSoundOptions")
    void verifyAllSoundOptionsAreListedInAudio(String option) {
        assertThat(getDocsContent("core-modules/audio.md")).contains(option);
    }

    @ParameterizedTest
    @MethodSource("dev.screwbox.core.test.TestSources#allGraphicConfigurationOptions")
    void verifyAllGraphicConfigurationOptionsAreListedInGraphics(String option) {
        assertThat(getDocsContent("core-modules/graphics/index.md")).contains(option);
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
