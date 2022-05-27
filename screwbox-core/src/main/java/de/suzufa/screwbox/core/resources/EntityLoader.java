package de.suzufa.screwbox.core.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.resources.internal.ConverterRegistry;

public class EntityLoader<T> {

    private final Map<Class<?>, ConverterRegistry<?>> registries = new HashMap<>();
    private final List<Extractor<T, ?>> extractors = new ArrayList<>();

    public <X> EntityLoader<T> add(Converter<X> converter, Class<X> acceptedClass) {
        getOrCreateRegistryForType(acceptedClass).register(converter);
        return this;
    }

    public EntityLoader<T> add(List<Extractor<T, ?>> extractors) {
        for (var extractor : extractors) {
            add(extractor);
        }
        return this;
    }

    public EntityLoader<T> add(Extractor<T, ?> extractor) {
        extractors.add(extractor);
        return this;
    }

    public List<Entity> createEnttiesFrom(final T input) {
        List<Entity> allEntities = new ArrayList<>();
        for (var convertible : extractConvertibleFrom(input)) {
            for (var reg : registries.values()) {
                if (reg.acceptsClass(convertible.getClass())) {
                    List<Entity> loaded = reg.load(convertible);
                    allEntities.addAll(loaded);
                }
            }
        }

        return allEntities;
    }

    private List<?> extractConvertibleFrom(final T input) {
        return extractors.stream()
                .flatMap(e -> e.extractFrom(input).stream())
                .toList();
    }

    private <X> ConverterRegistry<X> getOrCreateRegistryForType(Class<X> type) {
        if (!registries.containsKey(type)) {
            registries.put(type, new ConverterRegistry<>(type));
        }
        return (ConverterRegistry<X>) registries.get(type);
    }
}
