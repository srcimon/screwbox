package dev.screwbox.core.environment.bulkimport;

import dev.screwbox.core.environment.Entity;

import java.util.List;
import java.util.function.Function;

public class ImportProfile<T, I> {

    public static <T, I> ImportProfile<T, I> noIndexData(List<T> sources) {
        return indexedData(sources, in -> {
            throw new IllegalArgumentException("no index specified");
        });
    }
    public static <T, I> ImportProfile<T, I> indexedData(List<T> sources, Function<T, I> indexFunction) {
        return new ImportProfile<>();
    }

    public ImportProfile<T, I> test(T t) {
        return this;
    }

    public ImportProfile<T, I> assign(ImportCondition<T, I> condition, Blueprint<T> blueprint) {
        return this;
    }

    public ImportProfile<T, I> assign(I t, MultiEntityBlueprint<T> blueprint) {
        return this;
    }

    public ImportProfile<T, I> assign(I t, Blueprint<T> blueprint) {
        return this;
    }

    public List<Entity> createEntities(T source) {
        return null;
    }
}
