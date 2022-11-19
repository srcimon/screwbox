package de.suzufa.screwbox.core.assets;

import java.util.List;

/**
 * Preload {@link Asset}s in your game {@link Class}es.
 */
public interface Assets {

    /**
     * Loads all unloaded {@link Asset}s in the given package. Returns a list of all
     * loaded {@link Loadable}s.
     */
    List<Loadable> preparePackage(String packageName);

    /**
     * Start asynchronous loading of all unloaded {@link Asset}s in the given
     * package.
     */
    Assets preparePackageAsync(String packageName);

    /**
     * List all {@link Loadable} defined in the given package.
     */
    List<Loadable> listAssetsInPackage(String packageName);

    /**
     * Activates logging of loading results. Logging is disabled by default.
     */
    Assets enableLogging();

    /**
     * Disables logging of loading results. Logging is disabled by default.
     */
    Assets disableLogging();

}
