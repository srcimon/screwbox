package de.suzufa.screwbox.core.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.resources.internal.ConverterRegistry;

/**
 * The {@link EntityLoader} is used to load entities from any input source. The
 * {@link EntityLoader} uses {@link Extractor}s to extract an intermediate from
 * the input source. Then it uses {@link EntityConverter}s to create the
 * {@link Entity}s from the intermediate.
 */
public class EntityLoader<T> {

    private final Map<Class<?>, ConverterRegistry<?>> registries = new HashMap<>();
    private final List<Extractor<T, ?>> extractors = new ArrayList<>();

    /**
     * Adds a new {@link EntityConverter} used to convert the intermediate to
     * {@link Entity}s.
     */
    @SuppressWarnings("unchecked")
    public <X> EntityLoader<T> add(final EntityConverter<X> converter, final Class<X> acceptedClass) {
        if (!registries.containsKey(acceptedClass)) {
            registries.put(acceptedClass, new ConverterRegistry<>(acceptedClass));
        }
        final ConverterRegistry<X> orCreateRegistryForType = (ConverterRegistry<X>) registries.get(acceptedClass);
        orCreateRegistryForType.register(converter);
        return this;
    }

    /**
     * Adds multiple {@link Extractor}s used to extract intermediate from the input
     * source.
     */
    public EntityLoader<T> add(final List<Extractor<T, ?>> extractors) {
        for (final var extractor : extractors) {
            add(extractor);
        }
        return this;
    }

    /**
     * Adds a new {@link Extractor} used to extract intermediate from the input
     * source.
     */
    public EntityLoader<T> add(final Extractor<T, ?> extractor) {
        extractors.add(extractor);
        return this;
    }

    /**
     * Creates enties from the input source, using the given {@link Extractor}s and
     * {@link EntityConverter}s.
     * 
     * @see #add(Extractor)
     * @see #add(EntityConverter)
     */
    public List<Entity> createEnttiesFrom(final T input) {
        final List<Entity> entities = new ArrayList<>();
        for (final var convertible : extractConvertibleFrom(input)) {
            for (final var registry : registries.values()) {
                if (registry.acceptsClass(convertible.getClass())) {
                    final List<Entity> loaded = registry.load(convertible);
                    entities.addAll(loaded);
                }
            }
        }

        return entities;
    }

    private List<?> extractConvertibleFrom(final T input) {
        return extractors.stream()
                .flatMap(e -> e.extractFrom(input).stream())
                .toList();
    }
}
