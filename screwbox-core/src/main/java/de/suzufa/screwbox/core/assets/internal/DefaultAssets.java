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

    private final Cache<String, List<AssetLocation<?>>> LOCATIONS = new Cache<>();

    private final Log log;
    private Async async;

    private boolean logEnabled = false;

    public DefaultAssets(final Async async, final Log log) {
        this.async = async;
        this.log = log;
    }

    @Override
    public List<AssetLocation<?>> preparePackage(final String packageName) {
        final List<AssetLocation<?>> updatedLocations = new ArrayList<>();
        try {
            Time before = Time.now();
            final List<AssetLocation<?>> assetLocations = listAssetLocationsInPackage(packageName);
            for (final var assetLocation : assetLocations) {

                Asset<?> asset = assetLocation.asset();
                if (!asset.isLoaded()) {
                    asset.load();
                    updatedLocations.add(assetLocation);
                }
            }
            var durationMs = Duration.since(before).milliseconds();
            if (logEnabled) {

                log.debug(String.format("loaded %s assets in %,d ms", updatedLocations.size(), durationMs));
            }
        } catch (final RuntimeException e) {
            throw new RuntimeException("Exception loading assets", e);
        }

        return updatedLocations;
    }

    @Override
    public List<AssetLocation<?>> listAssetLocationsInPackage(final String packageName) {
        return LOCATIONS.getOrElse(packageName, () -> {
            final List<AssetLocation<?>> assetLocations = new ArrayList<>();
            for (final var clazz : new Demo().findAllClassesUsingClassLoader(packageName)) {
                for (final var field : clazz.getDeclaredFields()) {
                    if (Asset.class.equals(field.getType())) {
                        try {
                            // TODO: better warning when not canAccess field
                            boolean isAccessible = field.trySetAccessible();
                            if (!isAccessible) {
                                throw new IllegalStateException(
                                        "could not make field accessible for injecting asset");
                            }
                            final Asset<?> asset = (Asset<?>) field.get(Asset.class);
                            assetLocations.add(new AssetLocation<>(asset, clazz, field));

                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            throw new IllegalStateException("error fetching assets from " + packageName, e);
                        }
                    }
                }
            }
            return assetLocations;
        });
    }

    @Override
    public Assets preparePackageAsync(String packageName) {
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
