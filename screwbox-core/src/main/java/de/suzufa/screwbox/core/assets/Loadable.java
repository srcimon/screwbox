package de.suzufa.screwbox.core.assets;

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
