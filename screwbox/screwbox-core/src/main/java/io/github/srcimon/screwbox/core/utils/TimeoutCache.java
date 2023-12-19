package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TimeoutCache<K, V> extends Cache<K, V> {

    private final Duration lifetime;
    private final Map<K, Time> lifespans = new HashMap<>();

    public TimeoutCache(final Duration lifetime) {
        this.lifetime = Objects.requireNonNull(lifetime, "lifetime must not be null");
    }

    @Override
    public void put(final K key, final V value) {
        super.put(key, value);
        lifespans.put(key, Time.now().plus(lifetime));
    }

    @Override
    public Optional<V> get(final K key) {
        var value = super.get(key);
        if (value.isEmpty()) {
            return Optional.empty();
        }

        if (Time.now().isAfter(lifespans.get(key))) {
            super.clear(key);
            lifespans.remove(key);
            return Optional.empty();
        }
        return value;
    }

}
