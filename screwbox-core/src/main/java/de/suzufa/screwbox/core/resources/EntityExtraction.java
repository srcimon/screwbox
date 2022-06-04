package de.suzufa.screwbox.core.resources;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;

public class EntityExtraction<I, O> {

    private final List<O> entities;
    private final EntityExtractor<I> caller;
    private final List<Entity> extractedEntities = new ArrayList<>();

    public EntityExtraction(final List<O> extractedEntities, final EntityExtractor<I> caller) {// TODO: CYCLIC
                                                                                               // DEPENDENCY!
        this.entities = extractedEntities;
        this.caller = caller;
    }

    public EntityExtraction<I, O> convertVia(final EntityConverter<O> converter) {
        entities.stream().filter(converter::accepts).map(converter::convert).forEach(extractedEntities::add);
        return this;
    }

    public EntityExtractor<I> and() {
        caller.addEntities(extractedEntities);
        return caller;
    }
}
