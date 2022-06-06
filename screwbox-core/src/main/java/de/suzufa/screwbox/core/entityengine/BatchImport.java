package de.suzufa.screwbox.core.entityengine;

import java.util.List;

public class BatchImport<T> {

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

        public ExtractionLoop(final List<O> inputObjects, final C caller) {
            this.inputObjects = inputObjects;
            this.caller = caller;
        }

        public ExtractionLoop<C, O> addIf(Filter<O> filter, final Converter<O> converter) {
            inputObjects.stream()
                    .filter(filter::matches)
                    .map(converter::convert)
                    .forEach(engine::add);

            return this;
        }

        public ExtractionLoop<C, O> add(final Converter<O> converter) {
            inputObjects.stream()
                    .map(converter::convert)
                    .forEach(engine::add);

            return this;
        }

        public C endLoop() {
            return caller;
        }
    }

    private final T input;
    private EntityEngine engine;

    public BatchImport(final T input, EntityEngine engine) {
        this.input = input;
        this.engine = engine;
    }

    public BatchImport<T> addIf(final Filter<T> filter, final Converter<T> converter) {
        if (filter.matches(input)) {
            engine.add(converter.convert(input));
        }
        return this;
    }

    public BatchImport<T> add(final Converter<T> converter) {
        engine.add(converter.convert(input));
        return this;
    }

    public <O> ExtractionLoop<BatchImport<T>, O> forEach(final Extractor<T, O> extractor) {
        final List<O> extractedEntities = extractor.extractFrom(input);
        return new ExtractionLoop<>(extractedEntities, this);
    }

}
