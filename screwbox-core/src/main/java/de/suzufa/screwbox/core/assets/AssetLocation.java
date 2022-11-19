package de.suzufa.screwbox.core.assets;

import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Field;

//TODO: test and doc
public class AssetLocation<T> {

    private final Asset<T> asset;
    private final Field sourceField;

    public static AssetLocation<?> createAt(final Field field) {
        try {
            final boolean isAccessible = field.trySetAccessible();
            if (!isAccessible) {
                final String name = field.getDeclaringClass().getName() + "." + field.getName();
                throw new IllegalStateException("field is not accessible for creating asset location " + name);
            }
            final Asset<?> asset = (Asset<?>) field.get(Asset.class);
            return new AssetLocation<>(asset, field);

        } catch (IllegalArgumentException | IllegalAccessException e) {
            final String packageName = field.getClass().getPackageName();
            throw new IllegalStateException("error fetching assets from " + packageName, e);
        }
    }

    private AssetLocation(final Asset<T> asset, final Field sourceField) {
        this.asset = asset;
        this.sourceField = sourceField;
    }

    public static boolean isAssetLocation(final Field field) {
        return Asset.class.equals(field.getType()) && isStatic(field.getModifiers());
    }

    public Asset<T> asset() {
        return asset;
    }

    public Field sourceField() {
        return sourceField;
    }

}
