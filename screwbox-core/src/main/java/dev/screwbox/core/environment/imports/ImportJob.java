package dev.screwbox.core.environment.imports;

import dev.screwbox.core.environment.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ImportJob<T, I> {

    private final Function<T, I> indexFunction;
    private List<T> sources;
    private Map<ImportCondition<T, I>, ComplexBlueprint<T>> blueprints = new HashMap<>();

    public static <T, I> ImportJob<T, I> source(List<T> sources) {
        return indexedSource(sources, in -> {
            throw new IllegalArgumentException("no index specified");
        });
    }

    public static <T, I> ImportJob<T, I> indexedSource(List<T> sources, Function<T, I> indexFunction) {
        return new ImportJob<>(sources, indexFunction);
    }

    private ImportJob(List<T> sources, Function<T, I> indexFunction) {
        this.sources = sources;
        this.indexFunction = indexFunction;
    }

    public ImportJob<T, I> test(T t) {
        return this;
    }

    public ImportJob<T, I> assign(ImportCondition<T, I> condition, Blueprint<T> blueprint) {
        return this;
    }

    public ImportJob<T, I> assign(final I index, final ComplexBlueprint<T> blueprint) {
        blueprints.put(ImportCondition.index(index), blueprint);
        return this;
    }

    public ImportJob<T, I> assign(final I index, final Blueprint<T> blueprint) {
        blueprints.put(ImportCondition.index(index), upgradeBlueprint(blueprint));
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

    private static <T> ComplexBlueprint<T> upgradeBlueprint(Blueprint<T> blueprint) {
        return (source, context) -> List.of(blueprint.create(source));
    }
}
