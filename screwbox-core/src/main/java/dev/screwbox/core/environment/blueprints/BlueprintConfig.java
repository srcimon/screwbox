package dev.screwbox.core.environment.blueprints;

import dev.screwbox.core.environment.Entity;

import java.util.List;
import java.util.function.Function;

public class BlueprintConfig<T, I> {

    public static <T, I> BlueprintConfig<T, I> noIndexData(List<T> sources) {
        return indexedData(sources, in -> {
            throw new IllegalArgumentException("no index specified");
        });
    }
    public static <T, I> BlueprintConfig<T, I> indexedData(List<T> sources, Function<T, I> indexFunction) {
        return new BlueprintConfig<>();
    }

    public BlueprintConfig<T, I> test(T t) {
        return this;
    }

    public BlueprintConfig<T, I> assign(ImportCondition<T, I> condition, Blueprint<T> blueprint) {
        return this;
    }

    public BlueprintConfig<T, I> assign(I t, MultiEntityBlueprint<T> blueprint) {
        return this;
    }

    public BlueprintConfig<T, I> assign(I t, Blueprint<T> blueprint) {
        return this;
    }

    public List<Entity> createEntities(T source) {
        return null;
    }
}
