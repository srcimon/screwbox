package de.suzufa.screwbox.core.resources.internal;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.resources.EntityConverter;

@Deprecated
public class ConverterRegistry<T> {

    private final List<EntityConverter<T>> converters = new ArrayList<>();
    private final Class<T> acceptedClass;

    public ConverterRegistry(final Class<T> acceptedClass) {
        this.acceptedClass = acceptedClass;
    }

    public void register(final EntityConverter<T> converter) {
        converters.add(converter);
    }

    @SuppressWarnings("unchecked")
    public List<Entity> load(final Object object) {
        return converters.stream()
                .filter(c -> c.accepts((T) object))
                .map(c -> c.convert((T) object))
                .toList();
    }

    public boolean acceptsClass(final Class<?> clazz) {
        return acceptedClass.isAssignableFrom(clazz);
    }
}
