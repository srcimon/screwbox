package de.suzufa.screwbox.core.entityengine;

import java.util.List;
import java.util.function.Predicate;

public class SourceImport<T> {

    public interface Converter<T> {
        Entity convert(final T object);
    }

    private final List<T> inputs;
    private EntityEngine engine;
    private Predicate<T> condition = input -> true;

    public SourceImport(final List<T> inputs, EntityEngine engine) {
        this.inputs = inputs;
        this.engine = engine;
    }

//    public SourceImport<T> convertIf(final Predicate<T> filter, final Converter<T> converter) {
//        for (var input : inputs) {
//            if (filter.test(input)) {
//                engine.add(converter.convert(input));
//            }
//        }
//        return this;
//    }

    public SourceImport<T> importAs(final Converter<T> converter) {
        for (var input : inputs) {
            if (condition.test(input)) {
                engine.add(converter.convert(input));
            }
        }
        return this;
    }

    public SourceImport<T> on(final Predicate<T> condition) {
        this.condition = condition;
        return this;
    }

}
