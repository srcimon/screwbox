package de.suzufa.screwbox.core.entityengine;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class BatchImport<T> {

    public interface Converter<T> {
        Entity convert(final T object);
    }

    public class ExtractionLoop<C, O> {

        private final List<O> inputObjects;
        private final C caller;

        public ExtractionLoop(final List<O> inputObjects, final C caller) {
            this.inputObjects = inputObjects;
            this.caller = caller;
        }

        public ExtractionLoop<C, O> addIf(Predicate<O> filter, final Converter<O> converter) {
            inputObjects.stream()
                    .filter(filter::test)
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

    public BatchImport<T> addIf(final Predicate<T> filter, final Converter<T> converter) {
        if (filter.test(input)) {
            engine.add(converter.convert(input));
        }
        return this;
    }

    public BatchImport<T> add(final Converter<T> converter) {
        engine.add(converter.convert(input));
        return this;
    }

    public <O> ExtractionLoop<BatchImport<T>, O> forEach(final Function<T, List<O>> extractionMethod) {
        final List<O> extractedEntities = extractionMethod.apply(input);
        return new ExtractionLoop<>(extractedEntities, this);
    }

}
