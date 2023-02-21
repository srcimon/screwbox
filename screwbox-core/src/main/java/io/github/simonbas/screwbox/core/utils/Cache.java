package io.github.simonbas.screwbox.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class Cache<K, V> {

    private final Map<K, V> store = new HashMap<>();

    public void clear() {
        store.clear();
    }

    public void put(final K key, final V value) {
        store.put(key, value);
    }

    public Optional<V> get(final K key) {
        final V storeValue = store.get(key);
        return Optional.ofNullable(storeValue);
    }

    public V getOrElse(final K key, final Supplier<V> valueSupplier) {
        final var storeValue = get(key);
        if (storeValue.isEmpty()) {
            final V supplierValue = valueSupplier.get();
            put(key, supplierValue);
            return supplierValue;
        }
        return storeValue.get();
    }

}
