package dev.screwbox.core.environment;

import java.util.function.Function;

public class BlueprintImportOptions<T, I> {

    private BlueprintImportOptions(Function<T, I> indexFunction) {

    }

    public static <A, B> BlueprintImportOptions<A, B> indexBy(Function<A, B> indexFunction) {
        return new BlueprintImportOptions<>(indexFunction);
    }

    public BlueprintImportOptions<T, I> assign(T t) {
        return this;
    }

    public BlueprintImportOptions<T, I> assignIndex(I t) {
        return this;
    }
}
