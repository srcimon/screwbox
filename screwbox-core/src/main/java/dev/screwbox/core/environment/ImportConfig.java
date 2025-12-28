package dev.screwbox.core.environment;

import java.util.List;
import java.util.function.Function;

public class ImportConfig<T, I> {

    public static <T, I> ImportConfig<T, I> indexedSource(List<T> sources, Function<T, I> indexFunction) {
        return new ImportConfig<>();
    }

    public ImportConfig<T, I> test(T t) {
        return this;
    }

    public ImportConfig<T, I> assign(Condition<T, I> condition, Blueprint<T> blueprint) {
        return this;
    }

    public ImportConfig<T, I> assign(I t, ComplexBlueprint<T> blueprint) {
        return this;
    }

    public ImportConfig<T, I> assign(I t, Blueprint<T> blueprint) {
        return this;
    }
}
