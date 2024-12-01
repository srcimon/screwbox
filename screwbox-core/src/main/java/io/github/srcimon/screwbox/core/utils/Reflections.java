package io.github.srcimon.screwbox.core.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipFile;

import static java.util.Objects.requireNonNull;

public final class Reflections {

    private static final String SEPARATOR = FileSystems.getDefault().getSeparator();

    private Reflections() {
    }

    /**
     * Returns a list of all {@link Class}es in the given Package.
     */
    public static List<Class<?>> findClassesInPackage(final String packageName) {
        requireNonNull(packageName, "packageName must not be null");
        final List<Class<?>> clazzes = new ArrayList<>();
        for (final String resourceName : getClassResources(packageName)) {
            final String[] splittedResource = resourceName.split(SEPARATOR.replace("\\", "\\\\"));
            final String className = splittedResource[splittedResource.length - 1];
            String packagen = packageName + resourceName
                    .split(packageName.replace(".", SEPARATOR.replace("\\", "\\\\")))[1]
                    .replace(SEPARATOR, ".")
                    .replace(className, "");
            packagen = packagen.substring(0, packagen.length() - 1);
            clazzes.add(getClass(className, packagen));
        }
        return clazzes;
    }

    public static <T> T createInstance(final Class<T> clazz) {
        try {
            return tryGetDefaultConstructor(clazz)
                    .orElseThrow(() -> new IllegalStateException("cannot create instance of %s because class is missing default constrctor".formatted(clazz.getName())))
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("cannot create instance of " + clazz.getName(), e);
        }
    }

    private static <T> Optional<Constructor<T>> tryGetDefaultConstructor(final Class<T> clazz) {
        final var constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 0) {
           return Optional.empty();
        }
        return Optional.of((Constructor<T>) constructors[0]);
    }

    private static Class<?> getClass(final String className, final String packageName) {
        try {
            final String name = packageName + "." + className.substring(0, className.lastIndexOf('.'));
            return Class.forName(name);
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("could not find classes", e);
        }
    }

    private static List<String> getClassResources(final String packageName) {
        return Resources.classPathElements().stream()
                .flatMap(element -> getResources(element).stream())
                .filter(resource -> resource.endsWith(".class"))
                .filter(resource -> resource.contains(packageName.replace(".", SEPARATOR)))
                .toList();
    }

    private static List<String> getResources(final String element) {
        final File file = new File(element);
        return file.isDirectory()
                ? getResourcesFromDirectory(file)
                : getResourcesFromJarFile(file);

    }

    private static List<String> getResourcesFromJarFile(final File file) {
        final ArrayList<String> retval = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(file)) {
            final var entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                retval.add(entries.nextElement().getName());
            }
            return retval;
        } catch (final IOException e) {
            throw new IllegalStateException("could not load resrouces from jar file", e);
        }
    }

    private static List<String> getResourcesFromDirectory(final File directory) {
        final List<String> resources = new ArrayList<>();
        for (final File file : directory.listFiles()) {
            if (file.isDirectory()) {
                resources.addAll(getResourcesFromDirectory(file));
            } else {
                try {
                    resources.add(file.getCanonicalPath());
                } catch (final IOException e) {
                    throw new IllegalStateException("could not read resources from directory", e);
                }
            }
        }
        return resources;
    }
}