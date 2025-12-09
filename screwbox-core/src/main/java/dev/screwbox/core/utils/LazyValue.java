package dev.screwbox.core.utils;

import dev.screwbox.core.assets.Asset;

import java.util.function.Supplier;

/**
 * Wrapper for values that will be loaded only when needed to save precious cpu.
 * In contrast to {@link Asset} wrapper is not thread safe.
 *
 * @since 3.17.0
 */
public class LazyValue<T> {

    private final Supplier<T> initializationMethod;
    private boolean isEmpty = true;
    private T value;

    /**
     * Creates a new instance. Value supplier will be used when needed to initialize {@link #value()}.
     */
    public LazyValue(final Supplier<T> valueSupplier) {
        this.initializationMethod = valueSupplier;
    }

    /**
     * Will return the wrapped value. Will cause calculation on first use.
     */
    public T value() {
        if (isEmpty) {
            value = initializationMethod.get();
            isEmpty = false;
        }
        return value;
    }

}
