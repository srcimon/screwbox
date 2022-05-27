package de.suzufa.screwbox.core.resources;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;

public class GameConverter<G> {

    private class ConverterRegistry<T> {
        private List<Converter<T>> converters = new ArrayList<>();
        private Class<T> acceptedClass;

        public ConverterRegistry(Class<T> acceptedClass) {
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
            return acceptedClass.isAssignableFrom(clazz);
        }
    }

    private final java.util.Map<Class<?>, ConverterRegistry<?>> registries = new HashMap<>();
    private final List<Extractor<G, ?>> extractors = new ArrayList<>();

    public <T> GameConverter<G> add(Converter<T> converter, Class<T> acceptedClass) {
        getOrCreateRegistryForType(acceptedClass).register(converter);
        return this;
    }

    public GameConverter<G> add(List<Extractor<G, ?>> extractors) {
        for (var extractor : extractors) {
            add(extractor);
        }
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
            for (var reg : registries.values()) {
                if (reg.acceptsClass(object.getClass())) {
                    List<Entity> loaded = reg.load(object);
                    allEntities.addAll(loaded);
                }
            }
        }

        return allEntities;
    }

    private <T> ConverterRegistry<T> getOrCreateRegistryForType(Class<T> type) {
        ConverterRegistry<T> registry = (ConverterRegistry<T>) registries.get(type);
        if (nonNull(registry)) {
            return registry;
        }
        var registryCreated = new ConverterRegistry<>(type);
        registries.put(type, registryCreated);
        return registryCreated;
    }
}
