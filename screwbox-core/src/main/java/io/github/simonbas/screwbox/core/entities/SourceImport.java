package io.github.simonbas.screwbox.core.entities;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public final class SourceImport<T> {

    public interface Converter<T> {
        Entity convert(final T object);
    }

    public final class ConditionalSourceImport {

        private final Predicate<T> condition;
        private final SourceImport<T> caller;

        private ConditionalSourceImport(final Predicate<T> condition, final SourceImport<T> caller) {
            this.condition = condition;
            this.caller = caller;
        }

        public SourceImport<T> as(final Converter<T> converter) {
            inputs.stream()
                    .filter(condition)
                    .map(converter::convert)
                    .forEach(engine::add);

            return caller;
        }
    }

    public class IndexSourceImport<M> {

        private final Function<T, M> indexFunction;
        private final SourceImport<T> caller;

        private IndexSourceImport(final Function<T, M> indexFunction, final SourceImport<T> caller) {
            this.indexFunction = requireNonNull(indexFunction, "Index function must not be null");
            this.caller = caller;
        }

        public MatchingSourceImportWithKey<M> when(final M index) {
            return new MatchingSourceImportWithKey<>(this.indexFunction, this, index);
        }

        public SourceImport<T> stopUsingIndex() {
            return caller;
        }

    }

    public final class MatchingSourceImportWithKey<M> {

        private final IndexSourceImport<M> caller;
        private final Function<T, M> matcher;
        private final M index;

        private MatchingSourceImportWithKey(final Function<T, M> matcher, final IndexSourceImport<M> caller,
                final M index) {
            this.matcher = matcher;
            this.caller = caller;
            this.index = requireNonNull(index, "Index must not be null");
        }

        public IndexSourceImport<M> as(final Converter<T> converter) {
            inputs.stream()
                    .filter(input -> matcher.apply(input).equals(index))
                    .map(converter::convert)
                    .forEach(engine::add);

            return caller;
        }
    }

    private final List<T> inputs;
    private final Entities engine;

    public SourceImport(final List<T> inputs, final Entities engine) {
        this.inputs = inputs;
        this.engine = requireNonNull(engine, "Engine must not be null");
    }

    public SourceImport<T> as(final Converter<T> converter) {
        requireNonNull(converter, "Converter must not be null");
        for (final var input : inputs) {
            engine.add(converter.convert(input));
        }
        return this;
    }

    public ConditionalSourceImport when(final Predicate<T> condition) {
        return new ConditionalSourceImport(condition, this);
    }

    public <M> IndexSourceImport<M> usingIndex(final Function<T, M> indexFunction) {
        return new IndexSourceImport<>(indexFunction, this);
    }

}
