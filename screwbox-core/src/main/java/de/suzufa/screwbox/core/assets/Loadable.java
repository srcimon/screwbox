package de.suzufa.screwbox.core.assets;

/**
 * Marks loadable resources.
 */
public interface Loadable {

    /**
     * Loads the resource.
     */
    void load();

    /**
     * Returns {@code true} if the resource has already been loaded.
     * 
     * @return
     */
    boolean isLoaded();
}
