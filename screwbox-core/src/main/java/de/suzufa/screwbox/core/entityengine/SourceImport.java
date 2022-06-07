package de.suzufa.screwbox.core.entityengine;

import java.util.List;
import java.util.function.Predicate;

public class SourceImport<T> {

    public interface Converter<T> {
        Entity convert(final T object);
    }

    private final List<T> inputs;
    private EntityEngine engine;

    public SourceImport(final List<T> inputs, EntityEngine engine) {
        this.inputs = inputs;
        this.engine = engine;
    }

    public SourceImport<T> addIf(final Predicate<T> filter, final Converter<T> converter) {
        for (var input : inputs) {
            if (filter.test(input)) {
                engine.add(converter.convert(input));
            }
        }
        return this;
    }

    public SourceImport<T> add(final Converter<T> converter) {
        for (var input : inputs) {
            engine.add(converter.convert(input));
        }
        return this;
    }

}
