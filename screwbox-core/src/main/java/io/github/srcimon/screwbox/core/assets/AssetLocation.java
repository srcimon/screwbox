package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;

import java.lang.reflect.Field;
import java.util.Optional;

import static java.lang.reflect.Modifier.isStatic;

/**
 * Marks {@link Asset} positions in your game classes.
 */
public class AssetLocation {

    private final Field sourceField;
    private final Asset<?> asset;

    private Duration loadingDuration;

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
        loadingDuration = Duration.ofExecution(asset::load);
    }

    /**
     * Returns {@code true} if {@link Asset} is already loaded.
     */
    public boolean isLoaded() {
        return asset.isLoaded();
    }

    /**
     * Returns the {@link Duration} it took to load the {@link Asset}. Is empty when
     * there is no information on the loading {@link Duration} or when {@link Asset}
     * has not been loaded yet.
     * 
     * @see #isLoaded()
     */
    public Optional<Duration> loadingDuration() {
        return Optional.ofNullable(loadingDuration);
    }

    /**
     * Returns a unique id of the {@link AssetLocation}.
     */
    public String id() {
        return sourceField.getDeclaringClass().getName() + "." + sourceField.getName();
    }

    @Override
    public String toString() {
        return "AssetLocation [id=" + id() + ", isLoaded=" + isLoaded() + "]";
    }

}
