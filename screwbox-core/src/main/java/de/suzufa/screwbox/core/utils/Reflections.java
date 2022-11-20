package de.suzufa.screwbox.core.utils;

import static de.suzufa.screwbox.core.utils.Resources.classPathElements;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

public final class Reflections {

    private Reflections() {
    }

    /**
     * Returns a list of all {@link Class}es in the given Package.
     */
    public static List<Class<?>> findClassesInPackage(final String packageName) {
        requireNonNull(packageName, "packageName must not be null");
        final List<Class<?>> clazzes = new ArrayList<>();
        final Pattern classNamePattern = Pattern.compile(".*" + packageName + ".*\\.class");
        for (final String resourceName : getResources(classNamePattern)) {
            final String className = resourceName.split("/")[resourceName.split("/").length - 1];
            String packagen = packageName
                    + resourceName.split(packageName.replace(".", "/"))[1].replace("/", ".").replace(
                            className, "");
            packagen = packagen.substring(0, packagen.length() - 1);
            clazzes.add(getClass(className, packagen));
        }
        return clazzes;
    }

    private static Class<?> getClass(final String className, final String packageName) {
        try {
            final String name = packageName + "." + className.substring(0, className.lastIndexOf('.'));
            return Class.forName(name);
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("could not find classes", e);
        }
    }

    private static List<String> getResources(final Pattern pattern) {
        final List<String> retval = new ArrayList<>();
        for (final String element : classPathElements()) {
            retval.addAll(getResources(element, pattern));
        }
        return retval;
    }

    private static List<String> getResources(final String element, final Pattern pattern) {
        final File file = new File(element);
        final List<String> resources = file.isDirectory()
                ? getResourcesFromDirectory(file)
                : getResourcesFromJarFile(file);

        final var matchingResources = new ArrayList<String>();
        for (final var resource : resources) {
            if (pattern.matcher(resource).matches()) {
                matchingResources.add(resource);
            }
        }
        return matchingResources;
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
        final List<String> retval = new ArrayList<>();
        for (final File file : directory.listFiles()) {
            if (file.isDirectory()) {
                retval.addAll(getResourcesFromDirectory(file));
            } else {
                try {
                    retval.add(file.getCanonicalPath());
                } catch (final IOException e) {
                    throw new IllegalStateException("could not read resources from directory", e);
                }
            }
        }
        return retval;
    }
}