package de.suzufa.screwbox.core.assets;

import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Field;

/**
 * Marks {@link Asset} positions in your game classes.
 */
//TODO: finish javadoc and tests
public class AssetLocation {

    private final Field sourceField;

    public static AssetLocation createAt(final Field field) {
        if (!isAssetLocation(field)) {
            throw new IllegalStateException("field is no possible asset location: " + id(field));
        }
        final boolean isAccessible = field.trySetAccessible();
        if (!isAccessible) {
            final String name = field.getDeclaringClass().getName() + "." + field.getName();
            throw new IllegalStateException("field is not accessible for creating asset location " + name);
        }
        return new AssetLocation(field);
    }

    public static boolean isAssetLocation(final Field field) {
        return Asset.class.equals(field.getType()) && isStatic(field.getModifiers());
    }

    private AssetLocation(Field sourceField) {
        this.sourceField = sourceField;
    }

    /**
     * Loads the {@link Asset}.
     */
    public void load() {
        asAsset().load();
    }

    /**
     * Returns {@code true} if {@link Asset} is already loaded.
     * 
     * @return
     */
    public boolean isLoaded() {
        return asAsset().isLoaded();
    }

    /**
     * Returns a unique id of the {@link AssetLocation}.
     */
    public String id() {
        return id(sourceField);
    }

    private static String id(Field field) {
        return field.getDeclaringClass().getName() + "." + field.getName();
    }

    private Asset<?> asAsset() {
        try {
            return (Asset<?>) sourceField.get(Asset.class);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            final String packageName = sourceField.getClass().getPackageName();
            throw new IllegalStateException("error fetching assets from " + packageName, e);
        }
    }
}
