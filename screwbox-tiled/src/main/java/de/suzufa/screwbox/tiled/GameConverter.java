package de.suzufa.screwbox.tiled;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;

public class GameConverter<G> {

    private class ConverterRegistryNew<T> {
        private List<Converter<T>> converters = new ArrayList<>();
        private Class<T> acceptedClass;

        public ConverterRegistryNew(Class<T> acceptedClass) {
            this.acceptedClass = acceptedClass;
        }

        public void register(final Converter<T> converter) {
            converters.add(converter);
        }

        private List<Entity> load(final Object object) {
            return converters.stream()
                    .filter(c -> c.accepts((T) object))
                    .map(c -> c.convert((T) object))
                    .toList();
        }

        public boolean acceptsClass(Class<?> clazz) {
            return acceptedClass.equals(clazz);
        }
    }

    private final java.util.Map<Class<?>, ConverterRegistryNew<?>> registries = new HashMap<>();
    private final List<Extractor<G, ?>> extractors = new ArrayList<>();

    public <T> GameConverter<G> add(Converter<T> converter, Class<T> acceptedClass) {
        getOrCreateRegistryForType(acceptedClass).register(converter);
        return this;
    }

    public GameConverter<G> add(Extractor<G, ?> extractor) {
        extractors.add(extractor);
        return this;
    }

    public List<Entity> createEnttiesFrom(final G input) {
        List<Object> allInput = new ArrayList<>();
        for (var extractor : extractors) {
            allInput.addAll(extractor.extractFrom(input));
        }
        List<Entity> allEntities = new ArrayList<>();
        for (var object : allInput) {
            GameConverter<G>.ConverterRegistryNew<? extends Object> registry = getRegistryForType(object.getClass());
            List<Entity> loaded = registry.load(object);
            allEntities.addAll(loaded);
        }

        return allEntities;
    }

    private <T> ConverterRegistryNew<T> getRegistryForType(Class<T> type) {
        return (ConverterRegistryNew<T>) registries.get(type);
    }

    private <T> ConverterRegistryNew<T> getOrCreateRegistryForType(Class<T> type) {
        ConverterRegistryNew<T> registry = getRegistryForType(type);
        if (nonNull(registry)) {
            return registry;
        }
        var registryCreated = new ConverterRegistryNew<>(type);
        registries.put(type, registryCreated);
        return registryCreated;
    }
}
