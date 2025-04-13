package dev.screwbox.core.assets;

import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.graphics.SpriteBundle;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Preload {@link Asset}s in your game {@link Class}es.
 *
  * @see <a href="http://screwbox.dev/docs/core-modules/assets">Documentation</a>
 */
public interface Assets {

    /**
     * Loads all unloaded {@link AssetLocation}s in the given package. Returns a
     * list of all loaded {@link AssetLocation}s. {@link AssetLocation} will have
     * information on {@link AssetLocation#loadingDuration()}.
     *
     * @see #prepareClassPackage(Class)
     * @see #preparePackageAsync(String)
     */
    List<AssetLocation> preparePackage(String packageName);

    /**
     * Loads all unloaded {@link AssetLocation}s in the package of the given class. Returns a
     * list of all loaded {@link AssetLocation}s. {@link AssetLocation} will have
     * information on {@link AssetLocation#loadingDuration()}.
     *
     * @see #preparePackage(String)
     * @see #prepareClassPackageAsync(Class)
     */
    default List<AssetLocation> prepareClassPackage(final Class<?> clazz) {
        return preparePackage(getPackageName(clazz));
    }

    /**
     * Prepares all prepacked {@link AssetBundle}s in the {@link ScrewBox} game engine. Recommended when using
     * {@link SoundBundle}, {@link SpriteBundle} or {@link FontBundle}.
     *
     * @see #prepareEngineAssetsAsync()
     */
    default List<AssetLocation> prepareEngineAssets() {
        return prepareClassPackage(ScrewBox.class);
    }

    /**
     * Start asynchronous loading of all prepacked {@link AssetBundle}s in the {@link ScrewBox} game engine. Recommended
     * when using {@link SoundBundle}, {@link SpriteBundle} or {@link FontBundle}.
     *
     * @see #prepareEngineAssets()
     */
    default Assets prepareEngineAssetsAsync() {
        return prepareClassPackageAsync(ScrewBox.class);
    }

    /**
     * Start asynchronous loading of all unloaded {@link Asset}s in the given
     * package.
     */
    Assets preparePackageAsync(String packageName);

    /**
     * Start asynchronous loading of all unloaded {@link Asset}s in the package
     * of the given class.
     *
     * @see #prepareClassPackage(Class)
     */
    default Assets prepareClassPackageAsync(final Class<?> clazz) {
        return preparePackageAsync(getPackageName(clazz));
    }

    /**
     * List all {@link AssetLocation} defined in the given package.
     * {@link AssetLocation} will have no information on
     * {@link AssetLocation#loadingDuration()}.
     *
     * @see #listAssetLocationsInClassPackage(Class)
     */
    List<AssetLocation> listAssetLocationsInPackage(String packageName);

    /**
     * List all {@link AssetLocation} defined in the package of the given class.
     * {@link AssetLocation} will have no information on
     * {@link AssetLocation#loadingDuration()}.
     *
     * @see #listAssetLocationsInPackage(String)
     */
    default List<AssetLocation> listAssetLocationsInClassPackage(final Class<?> clazz) {
        return listAssetLocationsInPackage(getPackageName(clazz));
    }

    /**
     * Activates logging of loading results. Logging is disabled by default.
     */
    Assets enableLogging();

    /**
     * Disables logging of loading results. Logging is disabled by default.
     */
    Assets disableLogging();

    /**
     * Returns {@code true} if preparing is currently in progress.
     *
     * @see #prepareClassPackage(Class)
     * @see #preparePackage(String)
     */
    boolean isPreparing();

    private static String getPackageName(Class<?> clazz) {
        return requireNonNull(clazz, "class must not be null").getPackageName();
    }
}
