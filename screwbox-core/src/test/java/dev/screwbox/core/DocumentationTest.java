package dev.screwbox.core;

import dev.screwbox.core.assets.AssetBundle;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.utils.Cache;
import dev.screwbox.core.utils.Reflections;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;

class DocumentationTest {

    private static final Cache<String, String> DOC_CONTENT_CACHE = new Cache<>();

    @ParameterizedTest
    @MethodSource("allComponentClasses")
    void verifyComponentIsListedInComponentsOverview(Class<?> componentClazz) {
        assertThat(getDocsContent("reference/components-overview.md")).contains(componentClazz.getSimpleName());
    }

    @ParameterizedTest
    @MethodSource("allAssetBundles")
    void verifyAssetBundleIsListedInAssetDoc(Class<?> assetBundleClazz) {
        assertThat(getDocsContent("core-modules/assets.md")).contains(assetBundleClazz.getSimpleName());
    }

    @ParameterizedTest
    @EnumSource(Ease.class)
    void verifyAllEaseValuesAreListedInOverview(Ease ease) {
        assertThat(getDocsContent("reference/ease/index.md")).contains(ease.name());
    }

    @ParameterizedTest
    @EnumSource(SpriteBundle.class)
    void verifyAllSpritesAreListedInOverview(SpriteBundle spriteBundle) {
        assertThat(getDocsContent("reference/sprites.md")).contains(spriteBundle.name());
    }

    @ParameterizedTest
    @EnumSource(ShaderBundle.class)
    void verifyAllShadersAreListedInOverview(ShaderBundle shader) {
        assertThat(getDocsContent("reference/shaders/index.md")).contains(shader.name());
    }

    @ParameterizedTest
    @MethodSource("allSoundOptions")
    void verifyAllSoundOptionsAreListedInAudio(String option) {
        assertThat(getDocsContent("core-modules/audio.md")).contains(option);
    }

    @ParameterizedTest
    @MethodSource("allGraphicConfigurationOptions")
    void verifyAllGraphicConfigurationOptionsAreListedInGraphics(String option) {
        assertThat(getDocsContent("core-modules/graphics/index.md")).contains(option);
    }

    private static Stream<Arguments> allGraphicConfigurationOptions() {
        return Arrays.stream(GraphicsConfiguration.class.getDeclaredFields())
                .filter(not(field -> isStatic(field.getModifiers())))
                .map(Field::getName)
                .filter(not("listeners"::equals))
                .map(Arguments::of);
    }

    private static Stream<Arguments> allSoundOptions() {
        return Arrays.stream(SoundOptions.class.getDeclaredFields())
                .filter(not(field -> isStatic(field.getModifiers())))
                .map(Field::getName)
                .map(Arguments::of);
    }

    private static Stream<Arguments> allComponentClasses() {
        return Reflections.findClassesInPackage("dev.screwbox.core.environment").stream()
                .filter(Component.class::isAssignableFrom)
                .filter(not(Class::isMemberClass))
                .map(Arguments::of);
    }

    private static Stream<Arguments> allAssetBundles() {
        return Reflections.findClassesInPackage("dev.screwbox.core").stream()
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
