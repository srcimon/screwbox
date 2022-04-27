package de.suzufa.screwbox.tiled.internal;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.tiled.Converter;

public class ConverterRegistry<T> {

    private List<Converter<T>> converters = new ArrayList<>();

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

}
