package de.suzufa.screwbox.core.assets.internal;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.assets.AssetLocation;
import de.suzufa.screwbox.core.assets.Assets;
import de.suzufa.screwbox.core.assets.Demo;
import de.suzufa.screwbox.core.assets.LoadingProgress;
import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.utils.Cache;

public class DefaultAssets implements Assets {

    private final Cache<String, List<AssetLocation<?>>> LOCATIONS = new Cache<>();
    private final ExecutorService executor;
    private final Log log;

    private Future<?> loadingTask;
    private LoadingProgress loadingProgress;
    private final Consumer<Throwable> exceptionHandler;

    public DefaultAssets(final ExecutorService executor, final Consumer<Throwable> exceptionHandler, final Log log) {
        this.executor = executor;
        this.exceptionHandler = exceptionHandler;
        this.log = log;
    }

    @Override
    public Assets startLoadingFromPackage(final String packageName) {
        if (nonNull(loadingTask)) {
            throw new IllegalStateException("loading assets is already in progress");
        }
        loadingProgress = new LoadingProgress("searching for assets", 0);
        loadingTask = executor.submit(() -> {
            try {
                Time before = Time.now();
                final List<AssetLocation<?>> assetLocations = scanPackageForAssetLocations(packageName);
                for (final var assetLocation : assetLocations) {
                    loadingProgress = new LoadingProgress("loading asset " + assetLocation.id(),
                            assetLocations.size());
                    assetLocation.asset().load();
                }
                loadingTask = null;
                loadingProgress = null;
                var durationMs = Duration.since(before).milliseconds();
                log.info("loaded " + assetLocations.size() + " assets in " + durationMs + " ms");
            } catch (final RuntimeException e) {
                final var wrappedException = new RuntimeException("Exception loading assets", e);
                exceptionHandler.accept(wrappedException);
            }
        });

        return this;
    }

    @Override
    public List<AssetLocation<?>> scanPackageForAssetLocations(final String packageName) {
        return LOCATIONS.getOrElse(packageName, () -> {
            final List<AssetLocation<?>> assetLocations = new ArrayList<>();
            for (final var clazz : new Demo().findAllClassesUsingClassLoader(packageName)) {
                for (final var field : clazz.getDeclaredFields()) {
                    if (field.getType().isAssignableFrom(Asset.class)) {
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
    public boolean isLoadingInProgress() {
        return nonNull(loadingTask);
    }

    @Override
    public LoadingProgress loadingProgress() {
        return loadingProgress;
    }

}
