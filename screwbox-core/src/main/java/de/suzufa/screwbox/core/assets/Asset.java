package de.suzufa.screwbox.core.assets;

import java.util.Objects;
import java.util.function.Supplier;

//TODO: javadoc and test
public class Asset<T> {

    private final Supplier<T> supplier;
    private T value;

    public void load() {
        value = supplier.get();
        if (value == null) {
            throw new IllegalStateException("asset null after loading");
        }
    }

    public static <T> Asset<T> asset(final Supplier<T> supplier) {
        return new Asset<T>(supplier);
    }

    private Asset(final Supplier<T> supplier) {
        this.supplier = Objects.requireNonNull(supplier, "supplier must not be null");
    }

    public T get() {
        if (Objects.isNull(value)) {
            throw new IllegalStateException("asset has not been loaded yet");
        }
        return value;
    }
}
