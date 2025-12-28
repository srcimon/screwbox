package dev.screwbox.core.environment.imports;

import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ImportProfile<T, I> {

    private final Function<T, I> indexFunction;
    private List<T> sources;
    private Map<ImportCondition<T, I>, MultiEntityBlueprint<T>> blueprints = new HashMap<>();

    public static <T, I> ImportProfile<T, I> source(List<T> sources) {
        return indexedSource(sources, in -> {
            throw new IllegalArgumentException("no index specified");
        });
    }

    public static <T, I> ImportProfile<T, I> indexedSource(List<T> sources, Function<T, I> indexFunction) {
        return new ImportProfile<>(sources, indexFunction);
    }

    private ImportProfile(List<T> sources, Function<T, I> indexFunction) {
        this.sources = sources;
        this.indexFunction = indexFunction;
    }

    public ImportProfile<T, I> test(T t) {
        return this;
    }

    public ImportProfile<T, I> assign(ImportCondition<T, I> condition, Blueprint<T> blueprint) {
        return this;
    }

    public ImportProfile<T, I> assign(I t, MultiEntityBlueprint<T> blueprint) {
        blueprints.put(ImportCondition.index(t), blueprint);
        return this;
    }

    public ImportProfile<T, I> assign(I t, Blueprint<T> blueprint) {
        blueprints.put(ImportCondition.index(t), upgradeBlueprint(blueprint));
        return this;
    }

    public List<Entity> createEntities(T source, ImportContext context) {
        final I index = indexFunction.apply(source);
        return blueprints.entrySet().stream()
                .filter(entry -> (entry.getKey().matches(source, index)))
                .flatMap(entry -> entry.getValue().create(source, context).stream())
                .toList();
    }

    public List<T> sources() {
        return sources;
    }

    private static <T> MultiEntityBlueprint<T> upgradeBlueprint(Blueprint<T> blueprint) {
        return (source, context) -> List.of(blueprint.create(source));
    }
}
