package de.suzufa.screwbox.core.resources;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;

public class EntityExtractor<T> {

    private final T input;

    private final List<Entity> entities = new ArrayList<>();
    private List<EntityExtraction<EntityExtractor<T>, ?>> extractions = new ArrayList<>();

    public static <T> EntityExtractor<T> from(T input) {
        return new EntityExtractor<>(input);
    }

    private EntityExtractor(T input) {
        this.input = input;
    }

    public EntityExtractor<T> apply(final EntityConverter<T> converter) {
        if (converter.accepts(input)) {
            entities.add(converter.convert(input));
        }
        return this;
    }

    public <O> EntityExtraction<EntityExtractor<T>, O> extract(Extractor<T, O> extractor) {
        List<O> extractedEntities = extractor.extractFrom(input);
        var entityExtraction = new EntityExtraction<>(extractedEntities, this);
        extractions.add(entityExtraction);
        return entityExtraction;
    }

    public List<Entity> buildAllEntities() {
        extractions.stream().flatMap(e -> e.entities().stream()).forEach(entities::add);
        extractions.clear();
        return entities;
    }
}
