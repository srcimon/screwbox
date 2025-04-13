package dev.screwbox.core.assets.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.assets.AssetBundle;
import dev.screwbox.core.assets.AssetLocation;
import dev.screwbox.core.assets.Assets;
import dev.screwbox.core.async.Async;
import dev.screwbox.core.log.Log;
import dev.screwbox.core.utils.Cache;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.core.utils.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

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
        return ListUtil.merge(
                fetchAssetsFromPackage(packageName),
                fetchAssetBundlesFromPackage(packageName));
    }

    private List<AssetLocation> fetchAssetBundlesFromPackage(String packageName) {
        return Reflections.findClassesInPackage(packageName).stream()
                .filter(AssetBundle.class::isAssignableFrom)
                .filter(clazz -> !clazz.equals(AssetBundle.class))
                .map(clazz -> {
                    if (isNull(clazz.getEnumConstants())) {
                        throw new IllegalArgumentException("only enums are support to be asset bundles. %s is not an asset bundle".formatted(clazz));
                    }
                    return clazz;
                })
                .flatMap(clazz -> Stream.of(clazz.getEnumConstants()))
                .map(AssetBundle.class::cast)
                .map(AssetLocation::new)
                .toList();
    }

    private List<AssetLocation> fetchAssetsFromPackage(final String packageName) {
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