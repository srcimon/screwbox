package de.suzufa.screwbox.core.assets.internal;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.assets.AssetLocation;
import de.suzufa.screwbox.core.assets.Assets;
import de.suzufa.screwbox.core.assets.Demo;
import de.suzufa.screwbox.core.log.Log;

public class DefaultAssets implements Assets {

    private final ExecutorService executor;
    private final Log log;

    private Future<?> loadingTask;

    public DefaultAssets(final ExecutorService executor, final Log log) {
        this.executor = executor;
        this.log = log;
    }

    @Override
    public Assets startLoadingFromPackage(String packageName) {
        if (nonNull(loadingTask)) {
            throw new IllegalStateException("loading assets is already in progress");
        }
        loadingTask = executor.submit(() -> {
            List<AssetLocation<?>> assetLocations = scanPackageForAssetLocations(packageName);
            for (var assetLocation : assetLocations) {
                assetLocation.asset().load();
                log.debug("loading asset " + assetLocation.id() + " took "
                        + assetLocation.asset().loadingDuration().milliseconds() + " ms");
            }
            loadingTask = null;
        });

        return this;
    }

//TODO: caching!
    @Override
    public List<AssetLocation<?>> scanPackageForAssetLocations(String packageName) {
        List<AssetLocation<?>> assetLocations = new ArrayList<>();
        for (var clazz : new Demo().findAllClassesUsingClassLoader(packageName)) {
            for (var field : clazz.getDeclaredFields()) {
                if (field.getType().isAssignableFrom(Asset.class)) {
                    try {
                        Asset<?> asset = (Asset<?>) field.get(Asset.class);
                        assetLocations.add(new AssetLocation<>(asset, clazz, field));

                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new IllegalStateException("error fetching assets from " + packageName, e);
                    }
                }
            }
        }
        return assetLocations;
    }

    @Override
    public boolean isLoadingInProgress() {
        return nonNull(loadingTask);
    }

}
