package de.suzufa.screwbox.core.resources;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;

public class EntityExtractor<T> {

    private final T input;

    private final List<Entity> entities = new ArrayList<>();
    private List<EntityExtraction<EntityExtractor<T>, ?>> extractions = new ArrayList<>();

    public static <T> EntityExtractor<T> extractFrom(T input) {
        return new EntityExtractor<>(input);
    }

    private EntityExtractor(T input) {
        this.input = input;
    }

    void addEntities(List<Entity> entities) {
        this.entities.addAll(entities);
    }

    public <O> EntityExtraction<EntityExtractor<T>, O> extractVia(Extractor<T, O> extractor) {
        List<O> extractedEntities = extractor.extractFrom(input);
        var entityExtraction = new EntityExtraction<>(extractedEntities, this);
        extractions.add(entityExtraction);
        return entityExtraction;
    }

    public List<Entity> allEntities() {
        return extractions.stream().flatMap(e -> e.entities().stream()).toList();
    }
}
