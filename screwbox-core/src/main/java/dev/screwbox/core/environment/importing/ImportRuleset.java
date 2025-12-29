package dev.screwbox.core.environment.importing;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static dev.screwbox.core.environment.importing.ImportCondition.always;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * Ruleset used to import {@link Entity entities} into the {@link Environment}.
 *
 * @since 3.19.0
 */
public class ImportRuleset<T, I> {

    private final Function<T, I> indexFunction;
    private final List<T> sources;

    private final Map<ImportCondition<T, I>, ComplexBlueprint<T>> blueprints = new HashMap<>();

    /**
     * Creates a new instance for a single source without index.
     */
    public static <T, I> ImportRuleset<T, I> source(final T source) {
        requireNonNull(source, "source must not be null");
        return sources(List.of(source));
    }

    /**
     * Creates a new instance for a list of sources without index.
     */
    public static <T, I> ImportRuleset<T, I> sources(List<T> sources) {
        requireNonNull(sources, "sources must not be null");
        return new ImportRuleset<>(sources, null);
    }

    /**
     * Creates a new instance for a list of sources with index.
     */
    public static <T, I> ImportRuleset<T, I> indexedSources(List<T> sources, Function<T, I> indexFunction) {
        return new ImportRuleset<>(sources, indexFunction);
    }

    private ImportRuleset(final List<T> sources, final Function<T, I> indexFunction) {
        this.sources = new ArrayList<>(sources);
        this.indexFunction = indexFunction;
    }

    /**
     * Returns the sources of the ruleset.
     */
    public List<T> sources() {
        return Collections.unmodifiableList(sources);
    }

    /**
     * Returns {@code true} if the ruleset has an index.
     */
    public boolean hasIndex() {
        return nonNull(indexFunction);
    }

    /**
     * Creates an {@link Entity} using the specified {@link Blueprint} for each source.
     */
    public ImportRuleset<T, I> make(final Blueprint<T> blueprint) {
        assign(always(), upgradeBlueprint(blueprint));
        return this;
    }

    /**
     * Creates an {@link Entity} using the specified {@link AdvancedBlueprint} for each source.
     */
    public ImportRuleset<T, I> make(final AdvancedBlueprint<T> blueprint) {
        assign(always(), blueprint);
        return this;
    }

    /**
     * Creates {@link Entity entities} using the specified {@link ComplexBlueprint} for each source.
     */
    public ImportRuleset<T, I> makeMultiple(final ComplexBlueprint<T> blueprint) {
        assign(always(), blueprint);
        return this;
    }

    public ImportRuleset<T, I> assign(final I index, final Blueprint<T> blueprint) {
        assign(ImportCondition.index(index), upgradeBlueprint(blueprint));
        return this;
    }

    public ImportRuleset<T, I> assign(final I index, final AdvancedBlueprint<T> blueprint) {
        assign(ImportCondition.index(index), blueprint);
        return this;
    }

    public ImportRuleset<T, I> assign(final ImportCondition<T, I> condition, final Blueprint<T> blueprint) {
        assign(condition, upgradeBlueprint(blueprint));
        return this;
    }

    public ImportRuleset<T, I> assign(final ImportCondition<T, I> condition, final AdvancedBlueprint<T> blueprint) {
        assign(condition, upgradeBlueprint(blueprint));
        return this;
    }

    public ImportRuleset<T, I> makeMultiple(final I index, final ComplexBlueprint<T> blueprint) {
        assign(ImportCondition.index(index), blueprint);
        return this;
    }

    public ImportRuleset<T, I> assign(final ImportCondition<T, I> condition, final ComplexBlueprint<T> blueprint) {
        blueprints.put(condition, blueprint);
        return this;
    }

    public List<Entity> createEntities(final ImportContext context) {
        final List<Entity> entities = new ArrayList<>();
        for (final var source : sources) {
            final I index = isNull(indexFunction) ? null : indexFunction.apply(source);
            blueprints.entrySet().stream()
                    .filter(entry -> entry.getKey().matches(source, index))
                    .flatMap(entry -> entry.getValue().assembleFrom(source, context).stream())
                    .forEach(entities::add);
        }
        return entities;
    }

    private static <T> ComplexBlueprint<T> upgradeBlueprint(final Blueprint<T> blueprint) {
        return (source, context) -> List.of(blueprint.assembleFrom(source));
    }

    private static <T> ComplexBlueprint<T> upgradeBlueprint(final AdvancedBlueprint<T> blueprint) {
        return (source, context) -> List.of(blueprint.assembleFrom(source, context));
    }

}
