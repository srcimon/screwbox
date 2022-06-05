package de.suzufa.screwbox.core.resources;

import de.suzufa.screwbox.core.entityengine.Entity;

public interface EntityConverter<T> {

    Entity convert(final T object);
}
