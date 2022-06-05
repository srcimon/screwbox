package de.suzufa.screwbox.core.resources;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;

public class EntityExtraction<C, O> {

    private final List<O> inputObjects;
    private final C caller;
    private final List<Entity> extractedEntities = new ArrayList<>();

    public EntityExtraction(final List<O> extractedEntities, final C caller) {
        this.inputObjects = extractedEntities;
        this.caller = caller;
    }

    public EntityExtraction<C, O> use(final EntityConverter<O> converter) {
        inputObjects.stream()
                .filter(converter::accepts)
                .map(converter::convert)
                .forEach(extractedEntities::add);

        return this;
    }

    List<Entity> entities() {
        return extractedEntities;
    }

    public C endLoop() {
        return caller;
    }
}
