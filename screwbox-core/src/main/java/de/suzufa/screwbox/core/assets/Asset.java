package de.suzufa.screwbox.core.assets;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import de.suzufa.screwbox.core.Duration;

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

    private Duration loadingDuration;

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
     * very fast on {@link #get()}.
     */
    public void load() {
        if (!isLoaded()) {
            loadingDuration = Duration.ofExecution(() -> value = supplier.get());
            if (!isLoaded()) {
                throw new IllegalStateException("asset null after loading");
            }
        }
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
     * Returns the {@link Duration} it took to load the {@link Asset}. Throws
     * {@link IllegalStateException} when the {@link Asset} has not been loaded yet.
     * 
     * @see #isLoaded()
     */
    public Duration loadingDuration() {
        if (!isLoaded()) {
            throw new IllegalStateException("asset has not been loaded yet");
        }
        return loadingDuration;
    }
}
