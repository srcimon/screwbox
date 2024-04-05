package io.github.srcimon.screwbox.core.assets.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.AssetLocation;
import io.github.srcimon.screwbox.core.assets.Assets;
import io.github.srcimon.screwbox.core.assets.ScrewBoxFontBundle;
import io.github.srcimon.screwbox.core.async.Async;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultRenderer;
import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.utils.Cache;
import io.github.srcimon.screwbox.core.utils.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DefaultAssets implements Assets {

    private final Cache<String, List<AssetLocation>> cache = new Cache<>();

    private final Log log;
    private final Async async;

    private boolean logEnabled = false;

    public DefaultAssets(final Async async, final Log log) {
        this.async = async;
        this.log = log;
    }

    @Override
    public List<AssetLocation> preparePackage(final String packageName) {
        final Time before = Time.now();
        final var loadedAssets = new ArrayList<AssetLocation>();
        final var assets = listAssetLocationsInPackage(packageName);
        for (final var asset : assets) {
            if (!asset.isLoaded()) {
                asset.load();
                loadedAssets.add(asset);
            }
        }
        if (logEnabled) {
            log.debug("loaded %s assets in %s".formatted(loadedAssets.size(), Duration.since(before).humanReadable()));
        }

        if (loadedAssets.isEmpty()) {
            throw new IllegalStateException("no assets found to prepare");
        }

        return loadedAssets;
    }

    @Override
    public List<AssetLocation> listAssetLocationsInPackage(final String packageName) {
        return cache.getOrElse(packageName, () -> fetchAssetInPackage(packageName));
    }

    private List<AssetLocation> fetchAssetInPackage(final String packageName) {
        return Reflections.findClassesInPackage(packageName).stream()
                .flatMap(clazz -> Stream.of(clazz.getDeclaredFields()))
                .map(AssetLocation::tryToCreateAt)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
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

    @Override
    public boolean isPreparing() {
        return async.hasActiveTasks(Assets.class);
    }
}