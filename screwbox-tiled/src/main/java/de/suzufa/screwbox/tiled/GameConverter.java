package de.suzufa.screwbox.tiled;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;

public class GameConverter {

    private class ConverterRegistryNew<T> {
        private List<Converter<T>> converters = new ArrayList<>();
        private Class<T> acceptedClass;

        public ConverterRegistryNew(Class<T> acceptedClass) {
            this.acceptedClass = acceptedClass;
        }

        public void register(final Converter<T> converter) {
            converters.add(converter);
        }

        public List<Entity> load(final List<T> objects) {
            return objects.stream()
                    .flatMap(o -> load(o).stream())
                    .toList();
        }

        public List<Entity> load(final T object) {
            return converters.stream()
                    .filter(c -> c.accepts(object))
                    .map(c -> c.convert(object))
                    .toList();
        }

        public boolean acceptsClass(Class<?> clazz) {
            return acceptedClass.equals(clazz);
        }
    }

    private final java.util.Map<Class<?>, ConverterRegistryNew<?>> registries = new HashMap<>();

    public <T> GameConverter add(Converter<T> converter, Class<T> acceptedClass) {
        getRegistryForType(acceptedClass).register(converter);
        return this;
    }

    private <T> ConverterRegistryNew<T> getRegistryForType(Class<T> type) {
        ConverterRegistryNew<?> registry = registries.get(type);
        if (nonNull(registry)) {
            return (ConverterRegistryNew<T>) registry;
        }
        var registryCreated = new ConverterRegistryNew<>(type);
        registries.put(type, registryCreated);
        return registryCreated;
    }
}
