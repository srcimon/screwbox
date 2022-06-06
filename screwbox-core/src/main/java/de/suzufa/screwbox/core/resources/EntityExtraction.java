package de.suzufa.screwbox.core.resources;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.resources.EntityBuilder.Converter;
import de.suzufa.screwbox.core.resources.EntityBuilder.Filter;

public class EntityExtraction<C, O> {

    private final List<O> inputObjects;
    private final C caller;
    private final List<Entity> extractedEntities = new ArrayList<>();

    public EntityExtraction(final List<O> extractedEntities, final C caller) {
        this.inputObjects = extractedEntities;
        this.caller = caller;
    }

    public EntityExtraction<C, O> addIf(Filter<O> filter, final Converter<O> converter) {
        inputObjects.stream()
                .filter(filter::matches)
                .map(converter::convert)
                .forEach(extractedEntities::add);

        return this;
    }

    public EntityExtraction<C, O> use(final Converter<O> converter) {
        inputObjects.stream()
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
