package dev.screwbox.core.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ImportConfiguration<T, I> {

    private final Function<T, I> indexFunction;
    private List<T> sources;
    private Map<ImportCondition<T, I>, ComplexBlueprint<T>> blueprints = new HashMap<>();

    public static <T, I> ImportConfiguration<T, I> source(final T source) {
        return sources(List.of(source));
    }

    public static <T, I> ImportConfiguration<T, I> sources(List<T> sources) {
        return indexedSources(sources, in -> null);
    }

    public static <T, I> ImportConfiguration<T, I> indexedSources(List<T> sources, Function<T, I> indexFunction) {
        return new ImportConfiguration<>(sources, indexFunction);
    }

    private ImportConfiguration(List<T> sources, Function<T, I> indexFunction) {
        this.sources = new ArrayList<>(sources);
        this.indexFunction = indexFunction;
    }

    public ImportConfiguration<T, I> make(final Blueprint<T> blueprint) {
        assign(ImportCondition.always(), upgradeBlueprint(blueprint));
        return this;
    }

    public ImportConfiguration<T, I> make(final AdvancedBlueprint<T> blueprint) {
        assign(ImportCondition.always(), blueprint);
        return this;
    }

    public ImportConfiguration<T, I> make(final ComplexBlueprint<T> blueprint) {
        assign(ImportCondition.always(), blueprint);
        return this;
    }

    public ImportConfiguration<T, I> assign(final I index, final Blueprint<T> blueprint) {
        assign(ImportCondition.index(index), upgradeBlueprint(blueprint));
        return this;
    }

    public ImportConfiguration<T, I> assign(final I index, final AdvancedBlueprint<T> blueprint) {
        assign(ImportCondition.index(index), blueprint);
        return this;
    }

    public ImportConfiguration<T, I> assign(final ImportCondition<T, I> condition, final Blueprint<T> blueprint) {
        assign(condition, upgradeBlueprint(blueprint));
        return this;
    }

    public ImportConfiguration<T, I> assign(final ImportCondition<T, I> condition, final AdvancedBlueprint<T> blueprint) {
        assign(condition, upgradeBlueprint(blueprint));
        return this;
    }

    public ImportConfiguration<T, I> assign(final I index, final ComplexBlueprint<T> blueprint) {
        assign(ImportCondition.index(index), blueprint);
        return this;
    }

    public ImportConfiguration<T, I> assign(final ImportCondition<T, I> condition, final ComplexBlueprint<T> blueprint) {
        blueprints.put(condition, blueprint);
        return this;
    }

    public List<Entity> createEntities(T source, ImportContext context) {
        final I index = indexFunction.apply(source);
        return blueprints.entrySet().stream()
                .filter(entry -> entry.getKey().matches(source, index))
                .flatMap(entry -> entry.getValue().assembleFrom(source, context).stream())
                .toList();
    }

    public List<T> sources() {
        return sources;
    }

    private static <T> ComplexBlueprint<T> upgradeBlueprint(Blueprint<T> blueprint) {
        return (source, context) -> List.of(blueprint.assembleFrom(source));
    }

    private static <T> ComplexBlueprint<T> upgradeBlueprint(AdvancedBlueprint<T> blueprint) {
        return (source, context) -> List.of(blueprint.assembleFrom(source, context));
    }

}
