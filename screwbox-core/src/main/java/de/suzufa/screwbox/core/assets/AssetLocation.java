package de.suzufa.screwbox.core.assets;

/**
 * Marks {@link Asset} positions in your game classes.
 */
public interface AssetLocation {

    /**
     * Loads the {@link Asset}.
     */
    void load();

    /**
     * Returns {@code true} if {@link Asset} is already loaded.
     * 
     * @return
     */
    boolean isLoaded();
}
