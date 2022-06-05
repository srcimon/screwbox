package de.suzufa.screwbox.core.resources;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;

public class EntityBuilder<T> {

    private final T input;

    private final List<Entity> entities = new ArrayList<>();
    private final List<EntityExtraction<EntityBuilder<T>, ?>> extractions = new ArrayList<>();

    public static <T> EntityBuilder<T> forSource(final T input) {
        return new EntityBuilder<>(input);
    }

    private EntityBuilder(final T input) {
        this.input = input;
    }

    public EntityBuilder<T> addIf(final InputFilter<T> filter, final EntityConverter<T> converter) {
        if (filter.matches(input)) {
            entities.add(converter.convert(input));
        }
        return this;
    }

    public EntityBuilder<T> add(final EntityConverter<T> converter) {
        entities.add(converter.convert(input));
        return this;
    }

    public <O> EntityExtraction<EntityBuilder<T>, O> forEach(final Extractor<T, O> extractor) {
        final List<O> extractedEntities = extractor.extractFrom(input);
        final var entityExtraction = new EntityExtraction<>(extractedEntities, this);
        extractions.add(entityExtraction);
        return entityExtraction;
    }

    public List<Entity> build() {
        extractions.stream().flatMap(e -> e.entities().stream()).forEach(entities::add);
        extractions.clear();
        return entities;
    }

}
