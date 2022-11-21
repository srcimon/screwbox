package de.suzufa.screwbox.core.assets;

import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Field;
import java.util.Optional;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;

/**
 * Marks {@link Asset} positions in your game classes.
 */
public class AssetLocation {

    private final Field sourceField;
    private final Asset<?> asset;

    /**
     * Tries to create an {@link AssetLocation} from a {@link Field}. Is most likely
     * called internaly in the {@link Engine}. Is empty when {@link Field} is not an
     * {@link AssetLocation}.
     */
    public static Optional<AssetLocation> tryToCreateAt(final Field field) {
        if (!Asset.class.equals(field.getType()) || !isStatic(field.getModifiers())) {
            return Optional.empty();
        }
        final boolean isAccessible = field.trySetAccessible();
        if (!isAccessible) {
            final String name = field.getDeclaringClass().getName() + "." + field.getName();
            throw new IllegalStateException("field is not accessible for creating asset location " + name);
        }
        return Optional.of(new AssetLocation(field));
    }

    private AssetLocation(final Field sourceField) {
        this.sourceField = sourceField;
        try {
            this.asset = (Asset<?>) sourceField.get(Asset.class);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            final String packageName = sourceField.getClass().getPackageName();
            throw new IllegalStateException("error fetching assets from " + packageName, e);
        }
    }

    /**
     * Loads the {@link Asset}.
     */
    public void load() {
        asset.load();
    }

    /**
     * Returns {@code true} if {@link Asset} is already loaded.
     * 
     * @return
     */
    public boolean isLoaded() {
        return asset.isLoaded();
    }

    /**
     * Returns the {@link Duration} it took to load the {@link Asset}. Throws
     * {@link IllegalStateException} when the {@link Asset} has not been loaded yet.
     * 
     * @see #isLoaded()
     * @see #loadingTime
     */
    public Duration loadingDuration() {
        return asset.loadingDuration();
    }

    /**
     * Returns the {@link Time} the {@link Asset} loading finished. Throws
     * {@link IllegalStateException} when the {@link Asset} has not been loaded yet.
     * 
     * @see #isLoaded()
     * @see #loadingDuration
     */
    public Time loadingTime() {
        return asset.loadingTime();
    }

    /**
     * Returns a unique id of the {@link AssetLocation}.
     */
    public String id() {
        return sourceField.getDeclaringClass().getName() + "." + sourceField.getName();
    }

}
