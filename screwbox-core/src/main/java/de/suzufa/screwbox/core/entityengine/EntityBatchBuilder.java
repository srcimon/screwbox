package de.suzufa.screwbox.core.entityengine;

import java.util.ArrayList;
import java.util.List;

public class EntityBatchBuilder<T> {

    public interface Converter<T> {
        Entity convert(final T object);
    }

    public interface Filter<T> {
        boolean matches(T input);
    }

    public interface Extractor<I, O> {
        public List<O> extractFrom(I input);
    }

    public class ExtractionLoop<C, O> {

        private final List<O> inputObjects;
        private final C caller;
        private final List<Entity> extractedEntities = new ArrayList<>();

        public ExtractionLoop(final List<O> extractedEntities, final C caller) {
            this.inputObjects = extractedEntities;
            this.caller = caller;
        }

        public ExtractionLoop<C, O> addIf(Filter<O> filter, final Converter<O> converter) {
            inputObjects.stream()
                    .filter(filter::matches)
                    .map(converter::convert)
                    .forEach(extractedEntities::add);

            return this;
        }

        public ExtractionLoop<C, O> add(final Converter<O> converter) {
            inputObjects.stream()
                    .map(converter::convert)
                    .forEach(extractedEntities::add);

            return this;
        }

        public C endLoop() {
            return caller;
        }
    }

    private final T input;
    private final List<Entity> entities = new ArrayList<>();
    private final List<ExtractionLoop<EntityBatchBuilder<T>, ?>> extractions = new ArrayList<>();

    public static <T> EntityBatchBuilder<T> fromSource(final T input) {
        return new EntityBatchBuilder<>(input);
    }

    private EntityBatchBuilder(final T input) {
        this.input = input;
    }

    public EntityBatchBuilder<T> addIf(final Filter<T> filter, final Converter<T> converter) {
        if (filter.matches(input)) {
            entities.add(converter.convert(input));
        }
        return this;
    }

    public EntityBatchBuilder<T> add(final Converter<T> converter) {
        entities.add(converter.convert(input));
        return this;
    }

    public <O> ExtractionLoop<EntityBatchBuilder<T>, O> forEach(final Extractor<T, O> extractor) {
        final List<O> extractedEntities = extractor.extractFrom(input);
        final var entityExtraction = new ExtractionLoop<>(extractedEntities, this);
        extractions.add(entityExtraction);
        return entityExtraction;
    }

    public List<Entity> build() {
        extractions.stream().flatMap(e -> e.extractedEntities.stream()).forEach(entities::add);
        extractions.clear();
        return entities;
    }

}
