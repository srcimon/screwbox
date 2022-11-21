package de.suzufa.screwbox.core.assets;

import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Field;
import java.util.Optional;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;

/**
 * Marks {@link Asset} positions in your game classes.
 */
//TODO: finish javadoc and tests
public class AssetLocation {

    private final Field sourceField;

    public static Optional<AssetLocation> tryToCreateAt(final Field field) {
        if (!isAssetLocation(field)) {
            return Optional.empty();
        }
        final boolean isAccessible = field.trySetAccessible();
        if (!isAccessible) {
            final String name = field.getDeclaringClass().getName() + "." + field.getName();
            throw new IllegalStateException("field is not accessible for creating asset location " + name);
        }
        return Optional.of(new AssetLocation(field));
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
        toAsset().load();
    }

    /**
     * Returns {@code true} if {@link Asset} is already loaded.
     * 
     * @return
     */
    public boolean isLoaded() {
        return toAsset().isLoaded();
    }

    // TODO:Test
    /**
     * Returns the {@link Duration} it took to load the {@link Asset}. Throws
     * {@link IllegalStateException} when the {@link Asset} has not been loaded yet.
     * 
     * @see #isLoaded()
     * @see #loadingTime
     */
    public Duration loadingDuration() {
        return toAsset().loadingDuration();
    }

    // TODO:Test
    /**
     * Returns the {@link Time} the {@link Asset} loading finished. Throws
     * {@link IllegalStateException} when the {@link Asset} has not been loaded yet.
     * 
     * @see #isLoaded()
     * @see #loadingDuration
     */
    public Time loadingTime() {
        return toAsset().loadingTime();
    }

    /**
     * Returns a unique id of the {@link AssetLocation}.
     */
    public String id() {
        return sourceField.getDeclaringClass().getName() + "." + sourceField.getName();
    }

    private Asset<?> toAsset() {
        try {
            return (Asset<?>) sourceField.get(Asset.class);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            final String packageName = sourceField.getClass().getPackageName();
            throw new IllegalStateException("error fetching assets from " + packageName, e);
        }
    }
}
