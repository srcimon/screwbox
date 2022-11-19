package de.suzufa.screwbox.core.assets.internal;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.assets.AssetLocation;
import de.suzufa.screwbox.core.assets.Assets;
import de.suzufa.screwbox.core.async.Async;
import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.utils.Cache;

public class DefaultAssets implements Assets {

    private final Cache<String, List<AssetLocation<?>>> locationsCache = new Cache<>();

    private final Log log;
    private final Async async;

    private boolean logEnabled = false;

    public DefaultAssets(final Async async, final Log log) {
        this.async = async;
        this.log = log;
    }

    @Override
    public List<AssetLocation<?>> preparePackage(final String packageName) {
        final List<AssetLocation<?>> updatedLocations = new ArrayList<>();
        final Time before = Time.now();
        final List<AssetLocation<?>> assetLocations = listAssetLocationsInPackage(packageName);
        for (final var assetLocation : assetLocations) {

            final Asset<?> asset = assetLocation.asset();
            if (!asset.isLoaded()) {
                asset.load();
                updatedLocations.add(assetLocation);
            }
        }
        final var durationMs = Duration.since(before).milliseconds();
        if (logEnabled) {

            log.debug(String.format("loaded %s assets in %,d ms", updatedLocations.size(), durationMs));
        }

        return updatedLocations;
    }

    @Override
    public List<AssetLocation<?>> listAssetLocationsInPackage(final String packageName) {
        return locationsCache.getOrElse(packageName, () -> fetchAssetLocationsInPackage(packageName));
    }

    private List<AssetLocation<?>> fetchAssetLocationsInPackage(final String packageName) {
        var assetLocations = new ArrayList<AssetLocation<?>>();
        for (final var clazz : new Demo().findAllClassesUsingClassLoader(packageName)) {
            for (final var field : clazz.getDeclaredFields()) {
                if (AssetLocation.isAssetLocation(field)) {
                    assetLocations.add(AssetLocation.createAt(field));
                }
            }
        }
        return assetLocations;
    }

    @Override
    public Assets preparePackageAsync(final String packageName) {
        async.run(Assets.class, () -> preparePackage(packageName));
        return this;
    }

    @Override
    public Assets enableLogging() {
        logEnabled = true;
        return this;
    }

    @Override
    public Assets disableLogging() {
        logEnabled = false;
        return this;
    }

}
