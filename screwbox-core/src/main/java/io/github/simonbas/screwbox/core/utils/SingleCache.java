package io.github.simonbas.screwbox.core.utils;

import java.util.function.Supplier;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * Can be used to access a single value that is only once initialized.
 */
public class SingleCache<V> {

    private V value;

    /**
     * Gets the value or else the value of the {@link Supplier}. The {@link Supplier}s value is then stored for any further access.
     */
    public V getOrElse(final Supplier<V> valueSupplier) {
        requireNonNull(valueSupplier, "value supplier must not be null");
        if (isNull(value)) {
            value = valueSupplier.get();
        }
        return value;
    }
}
