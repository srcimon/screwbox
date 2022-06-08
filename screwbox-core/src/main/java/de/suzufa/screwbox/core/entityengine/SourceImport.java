package de.suzufa.screwbox.core.entityengine;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class SourceImport<T> {

    public interface Converter<T> {
        Entity convert(final T object);
    }

    public class ConditionalSourceImport {

        private final Predicate<T> condition;
        private final SourceImport<T> caller;

        private ConditionalSourceImport(final Predicate<T> condition, final SourceImport<T> caller) {
            this.condition = condition;
            this.caller = caller;
        }

        public SourceImport<T> as(final Converter<T> converter) {
            for (final var input : inputs) {
                if (condition.test(input)) {
                    engine.add(converter.convert(input));
                }
            }
            return caller;
        }
    }

    public class MatchingSourceImport<M> {

        private Function<T, M> matcher;

        public MatchingSourceImport(Function<T, M> matcher) {
            this.matcher = matcher;
        }

        public MatchingSourceImportWithKey<M> when(M key) {
            return new MatchingSourceImportWithKey<>(this.matcher, this, key);
        }

    }

    public class MatchingSourceImportWithKey<M> {

        private MatchingSourceImport<M> caller;
        private Function<T, M> matcher;
        private M key;

        public MatchingSourceImportWithKey(Function<T, M> matcher, MatchingSourceImport<M> caller, M key) {
            this.matcher = matcher;
            this.caller = caller;
            this.key = key;
        }

        public MatchingSourceImport<M> as(Converter<T> converter) {
            for (final var input : inputs) {
                if (matcher.apply(input).equals(key)) {
                    engine.add(converter.convert(input));
                }
            }
            return caller;
        }

    }

    private final List<T> inputs;
    private final EntityEngine engine;

    public SourceImport(final List<T> inputs, final EntityEngine engine) {
        this.inputs = inputs;
        this.engine = engine;
    }

    public SourceImport<T> as(final Converter<T> converter) {
        for (final var input : inputs) {
            engine.add(converter.convert(input));
        }
        return this;
    }

    public ConditionalSourceImport when(final Predicate<T> condition) {
        return new ConditionalSourceImport(condition, this);
    }

    public <M> MatchingSourceImport<M> usingIndex(Function<T, M> matcher) {
        return new MatchingSourceImport<>(matcher);
    }

}
