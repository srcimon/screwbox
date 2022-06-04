package de.suzufa.screwbox.core.resources;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;

public class EntityExtraction<C, O> {

    private final List<O> entities;
    private final C caller;
    private final List<Entity> extractedEntities = new ArrayList<>();

    public EntityExtraction(final List<O> extractedEntities, C caller) {
        this.entities = extractedEntities;
        this.caller = caller;
    }

    public EntityExtraction<C, O> convertVia(final EntityConverter<O> converter) {
        entities.stream().filter(converter::accepts).map(converter::convert).forEach(extractedEntities::add);
        return this;
    }

    List<Entity> entities() {
        return extractedEntities;
    }

    public C and() {
        return caller;
    }
}
