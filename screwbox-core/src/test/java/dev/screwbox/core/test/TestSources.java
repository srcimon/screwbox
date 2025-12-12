package dev.screwbox.core.test;

import dev.screwbox.core.assets.AssetBundle;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.utils.Reflections;
import org.junit.jupiter.params.provider.Arguments;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.function.Predicate.not;

public class TestSources {

    private TestSources() {}

    public static Stream<Arguments> allComponentClasses() {
        return Reflections.findClassesInPackage("dev.screwbox.core.environment").stream()
                .filter(dev.screwbox.core.environment.Component.class::isAssignableFrom)
                .filter(not(Class::isMemberClass))
                .filter(not(Component.class::equals))
                .map(Arguments::of);
    }


    public static Stream<Arguments> allGraphicConfigurationOptions() {
        return Arrays.stream(GraphicsConfiguration.class.getDeclaredFields())
                .filter(not(field -> isStatic(field.getModifiers())))
                .map(Field::getName)
                .filter(not("listeners"::equals))
                .map(Arguments::of);
    }

    public static Stream<Arguments> allSoundOptions() {
        return Arrays.stream(SoundOptions.class.getDeclaredFields())
                .filter(not(field -> isStatic(field.getModifiers())))
                .map(Field::getName)
                .map(Arguments::of);
    }

    public static Stream<Arguments> allAssetBundles() {
        return Reflections.findClassesInPackage("dev.screwbox.core").stream()
                .filter(AssetBundle.class::isAssignableFrom)
                .filter(not(Class::isMemberClass))
                .map(Arguments::of);
    }

}
