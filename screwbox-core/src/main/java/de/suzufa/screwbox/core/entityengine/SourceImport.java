package de.suzufa.screwbox.core.entityengine;

import java.util.List;
import java.util.function.Predicate;

public class SourceImport<T> {

    public interface Converter<T> {
        Entity convert(final T object);
    }

    public class ConditionalSourceImport {

        private Predicate<T> condition;
        private SourceImport<T> caller;

        private ConditionalSourceImport(Predicate<T> condition, SourceImport<T> caller) {
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

}
