package dev.screwbox.core.environment;

import java.util.List;
import java.util.function.Function;

public class BlueprintImport<T, I> {

    public static <T, I> BlueprintImport<T, I> noIndex(List<T> sources) {
        return indexed(sources, in -> {
            throw new IllegalArgumentException("no index specified");
        });
    }
    public static <T, I> BlueprintImport<T, I> indexed(List<T> sources, Function<T, I> indexFunction) {
        return new BlueprintImport<>();
    }

    public BlueprintImport<T, I> test(T t) {
        return this;
    }

    public BlueprintImport<T, I> assign(Condition<T, I> condition, Blueprint<T> blueprint) {
        return this;
    }

    public BlueprintImport<T, I> assign(I t, ComplexBlueprint<T> blueprint) {
        return this;
    }

    public BlueprintImport<T, I> assign(I t, Blueprint<T> blueprint) {
        return this;
    }
}
