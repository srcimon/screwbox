package de.suzufa.screwbox.core.assets;

import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Field;

//TODO: test and doc
public record AssetLocation<T> (Asset<T> asset, Field sourceField) {

    public static AssetLocation<?> createAt(final Field field) {
        try {
            final boolean isAccessible = field.trySetAccessible();
            if (!isAccessible) {
                String name = field.getDeclaringClass().getName() + "." + field.getName();
                throw new IllegalStateException("field is not accessible for creating asset location " + name);
            }
            final Asset<?> asset = (Asset<?>) field.get(Asset.class);
            return new AssetLocation<>(asset, field);

        } catch (IllegalArgumentException | IllegalAccessException e) {
            String packageName = field.getClass().getPackageName();
            throw new IllegalStateException("error fetching assets from " + packageName, e);
        }
    }

    public static boolean isAssetLocation(final Field field) {
        return Asset.class.equals(field.getType()) && isStatic(field.getModifiers());
    }
}
