package de.suzufa.screwbox.core.resources;

import java.util.List;

public class EntityExtraction<I, O> {

    private List<O> entities;
    private EntityExtractor<I> caller;

    public EntityExtraction(List<O> extractedEntities, EntityExtractor<I> caller) {// TODO: CYCLIC DEPENDENCY!
        this.entities = extractedEntities;
        this.caller = caller;
    }

}
