package dev.screwbox.core.environment;

import dev.screwbox.core.Percent;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public final class SourceImport<T> {

    private static final Random RANDOM = new Random();

    @FunctionalInterface
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
            return randomlyAs(converter, Percent.max());
        }

        /**
         * Imports the source using the specified {@link Converter} using the specified probability.
         *
         * @since 3.6.0
         */
        public SourceImport<T> randomlyAs(final Converter<T> converter, final Percent probability) {
            inputs.stream()
                    .filter(condition)
                    .map(converter::convert)
                    .filter(input -> probability.isMax() || RANDOM.nextDouble() <= probability.value())
                    .forEach(engine::addEntity);
            return caller;
        }
    }

    public class IndexSourceImport<M> {

        private final Function<T, M> indexFunction;

        private IndexSourceImport(final Function<T, M> indexFunction) {
            this.indexFunction = requireNonNull(indexFunction, "index function must not be null");
        }

        public MatchingSourceImportWithKey<M> when(final M index) {
            return new MatchingSourceImportWithKey<>(this.indexFunction, this, index);
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
            this.index = requireNonNull(index, "index must not be null");
        }

        /**
         * Imports the source using the specified {@link Converter} using the specified probability.
         *
         * @since 3.6.0
         */
        public IndexSourceImport<M> randomlyAs(final Converter<T> converter, final Percent probability) {
            inputs.stream()
                    .filter(input -> matcher.apply(input).equals(index))
                    .filter(input -> probability.isMax() || RANDOM.nextDouble() <= probability.value())
                    .map(converter::convert)
                    .forEach(engine::addEntity);

            return caller;
        }

        /**
         * Imports the source using the specified {@link Converter}.
         */
        public IndexSourceImport<M> as(final Converter<T> converter) {
            return randomlyAs(converter, Percent.max());
        }
    }

    private final List<T> inputs;
    private final Environment engine;

    public SourceImport(final List<T> inputs, final Environment engine) {
        this.inputs = inputs;
        this.engine = requireNonNull(engine, "Engine must not be null");
    }

    public SourceImport<T> as(final Converter<T> converter) {
        requireNonNull(converter, "Converter must not be null");
        for (final var input : inputs) {
            engine.addEntity(converter.convert(input));
        }
        return this;
    }

    public ConditionalSourceImport when(final Predicate<T> condition) {
        return new ConditionalSourceImport(condition, this);
    }

    public <M> IndexSourceImport<M> usingIndex(final Function<T, M> indexFunction) {
        return new IndexSourceImport<>(indexFunction);
    }
}