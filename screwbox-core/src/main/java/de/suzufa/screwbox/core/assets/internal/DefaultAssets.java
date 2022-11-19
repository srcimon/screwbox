package de.suzufa.screwbox.core.assets.internal;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.assets.Assets;
import de.suzufa.screwbox.core.async.Async;
import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.utils.Cache;
import de.suzufa.screwbox.core.utils.Reflections;

public class DefaultAssets implements Assets {

    private final Cache<String, List<Asset<?>>> cache = new Cache<>();

    private final Log log;
    private final Async async;

    private boolean logEnabled = false;

    public DefaultAssets(final Async async, final Log log) {
        this.async = async;
        this.log = log;
    }

    @Override
    public List<Asset<?>> preparePackage(final String packageName) {
        final Time before = Time.now();
        final var loadedAssets = new ArrayList<Asset<?>>();
        final var assets = listAssetsInPackage(packageName);
        for (final var asset : assets) {
            if (!asset.isLoaded()) {
                asset.load();
                loadedAssets.add(asset);
            }
        }
        final var durationMs = Duration.since(before).milliseconds();
        if (logEnabled) {
            log.debug(format("loaded %s assets in %,d ms", loadedAssets.size(), durationMs));
        }

        return loadedAssets;
    }

    @Override
    public List<Asset<?>> listAssetsInPackage(final String packageName) {
        return cache.getOrElse(packageName, () -> fetchAssetInPackage(packageName));
    }

    private List<Asset<?>> fetchAssetInPackage(final String packageName) {
        final var assets = new ArrayList<Asset<?>>();
        for (final var clazz : Reflections.findClassesInPackage(packageName)) {
            for (final var field : clazz.getDeclaredFields()) {
                if (isAssetLocation(field)) {
                    assets.add(createAt(field));
                }
            }
        }
        return assets;
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

    private Asset<?> createAt(final Field field) {
        try {
            final boolean isAccessible = field.trySetAccessible();
            if (!isAccessible) {
                final String name = field.getDeclaringClass().getName() + "." + field.getName();
                throw new IllegalStateException("field is not accessible for creating asset location " + name);
            }
            return (Asset<?>) field.get(Asset.class);

        } catch (IllegalArgumentException | IllegalAccessException e) {
            final String packageName = field.getClass().getPackageName();
            throw new IllegalStateException("error fetching assets from " + packageName, e);
        }
    }

    private boolean isAssetLocation(final Field field) {
        return Asset.class.equals(field.getType()) && isStatic(field.getModifiers());
    }
}
