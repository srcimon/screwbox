package de.suzufa.screwbox.core.resources;

import de.suzufa.screwbox.core.entityengine.Entity;

public interface Converter<T> {

    boolean accepts(final T object);

    Entity convert(final T object);
}
