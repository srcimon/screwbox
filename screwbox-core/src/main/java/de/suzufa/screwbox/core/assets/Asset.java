package de.suzufa.screwbox.core.assets;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

//TODO: javadoc
public class Asset<T> {

    private final Supplier<T> supplier;
    private T value;

    public static <T> Asset<T> asset(final Supplier<T> supplier) {
        return new Asset<T>(supplier);
    }

    public boolean isLoaded() {
        return nonNull(value);
    }

    public void load() {
        value = supplier.get();
        if (!isLoaded()) {
            throw new IllegalStateException("asset null after loading");
        }
    }

    private Asset(final Supplier<T> supplier) {
        this.supplier = requireNonNull(supplier, "supplier must not be null");
    }

    public T get() {
        if (!isLoaded()) {
            throw new IllegalStateException("asset has not been loaded yet");
        }
        return value;
    }
}
