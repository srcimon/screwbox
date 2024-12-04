package io.github.srcimon.screwbox.core.assets;

import java.util.function.Supplier;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * {@link Asset} is used to wrap your i/o intense game resources like graphics
 * and sounds when loaded for the first time. Assets can be preloaded via
 * {@link Assets#preparePackage(String)} or
 * {@link Assets#preparePackageAsync(String)}.
 * <p>
 * To make this preloading work {@link Asset}s have to be static class fields.
 * It's recommended to make them {@code private static final}.
 */
public class Asset<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private T value;

    /**
     * Creates a new {@link Asset}.
     *
     * @param supplier the supplier of the actual game resource.
     */
    public static <T> Asset<T> asset(final Supplier<T> supplier) {
        return new Asset<>(supplier);
    }

    private Asset(final Supplier<T> supplier) {
        this.supplier = requireNonNull(supplier, "supplier must not be null");
    }

    /**
     * Returns {@code true} if the {@link Asset} has been loaded.
     */
    public boolean isLoaded() {
        return nonNull(value);
    }

    /**
     * Loads the actual resource in the {@link Asset} wrapper so it can be received
     * very fast on {@link #get()}. Returns true if the {@link Asset} was actually loaded. Returns false if the {@link Asset} has been loaded before.
     */
    public boolean load() {
        if (isLoaded()) {
            return false;
        }
        value = supplier.get();
        if (!isLoaded()) {
            throw new IllegalStateException("asset is null after loading");
        }
        return true;
    }

    /**
     * Retrieves the {@link Asset} actual resource. If the resource has not been
     * loaded yet the resource will be loaded before.
     */
    @Override
    public T get() {
        if (!isLoaded()) {
            load();
        }
        return value;
    }

    /**
     * Removes already loaded content. Does nothing when not loaded.
     */
    public void unload() {
        value = null;
    }
}