package de.suzufa.screwbox.core.resources;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.resources.internal.ConverterRegistry;

public class EntityLoader<T> {

    private final java.util.Map<Class<?>, ConverterRegistry<?>> registries = new HashMap<>();
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
        List<Object> allInput = new ArrayList<>();
        for (var extractor : extractors) {
            allInput.addAll(extractor.extractFrom(input));
        }
        List<Entity> allEntities = new ArrayList<>();
        for (var object : allInput) {
            for (var reg : registries.values()) {
                if (reg.acceptsClass(object.getClass())) {
                    List<Entity> loaded = reg.load(object);
                    allEntities.addAll(loaded);
                }
            }
        }

        return allEntities;
    }

    private <X> ConverterRegistry<X> getOrCreateRegistryForType(Class<X> type) {
        ConverterRegistry<X> registry = (ConverterRegistry<X>) registries.get(type);
        if (nonNull(registry)) {
            return registry;
        }
        var registryCreated = new ConverterRegistry<>(type);
        registries.put(type, registryCreated);
        return registryCreated;
    }
}
