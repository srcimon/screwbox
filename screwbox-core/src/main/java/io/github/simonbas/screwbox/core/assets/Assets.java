package io.github.simonbas.screwbox.core.assets;

import java.util.List;

/**
 * Preload {@link Asset}s in your game {@link Class}es.
 */
public interface Assets {

    /**
     * Loads all unloaded {@link AssetLocation}s in the given package. Returns a
     * list of all loaded {@link AssetLocation}s. {@link AssetLocation} will have
     * information on {@link AssetLocation#loadingDuration()}.
     */
    List<AssetLocation> preparePackage(String packageName);

    /**
     * Start asynchronous loading of all unloaded {@link Asset}s in the given
     * package.
     */
    Assets preparePackageAsync(String packageName);

    /**
     * List all {@link AssetLocation} defined in the given package.
     * {@link AssetLocation} will have no information on
     * {@link AssetLocation#loadingDuration()}.
     */
    List<AssetLocation> listAssetLocationsInPackage(String packageName);

    /**
     * Activates logging of loading results. Logging is disabled by default.
     */
    Assets enableLogging();

    /**
     * Disables logging of loading results. Logging is disabled by default.
     */
    Assets disableLogging();

    boolean isPreparing();
}
