package dev.screwbox.core.utils;

import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Simple cache implementation used to store values that are expensive to retrieve.
 * Cache is serializable, but it will have lose all content when deserialized.
 * This will make it easier to use the cache in serializable classes.
 */
public class Cache<K, V> implements Serializable {

    private transient Map<K, V> store = new HashMap<>();

    @Serial
    private void readObject(final ObjectInputStream in) {
        store = new HashMap<>();
    }

    public void clear(final K key) {
        store.remove(key);
    }

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

    /**
     * Returns the current element count in the cache.
     *
     * @since 2.15.0
     */
    public int size() {
        return store.size();
    }
}
