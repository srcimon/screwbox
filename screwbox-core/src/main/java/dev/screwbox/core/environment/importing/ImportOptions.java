package dev.screwbox.core.environment.importing;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static dev.screwbox.core.environment.importing.ImportCondition.always;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * Ruleset used to import {@link Entity entities} into the {@link Environment}.
 *
 * @since 3.19.0
 */
public class ImportOptions<S, I> {

    public record ImportAssignment<S, I>(ImportCondition<S, I> condition, ComplexBlueprint<S> blueprint) {

    }

    private final Function<S, I> indexFunction;
    private final List<S> sources;
    private final List<ImportAssignment<S, I>> assignments = new ArrayList<>();

    /**
     * Creates a new instance for a single source without index.
     */
    public static <S, I> ImportOptions<S, I> source(final S source) {
        requireNonNull(source, "source must not be null");
        return sources(List.of(source));
    }

    /**
     * Creates a new instance for a list of sources without index.
     */
    public static <S, I> ImportOptions<S, I> sources(List<S> sources) {
        requireNonNull(sources, "sources must not be null");
        return new ImportOptions<>(sources, null);
    }

    /**
     * Creates a new instance for a list of sources with index.
     */
    public static <S, I> ImportOptions<S, I> indexedSources(List<S> sources, Function<S, I> indexFunction) {
        return new ImportOptions<>(sources, indexFunction);
    }

    private ImportOptions(final List<S> sources, final Function<S, I> indexFunction) {
        this.sources = new ArrayList<>(sources);
        this.indexFunction = indexFunction;
    }

    /**
     * Returns the sources of the ruleset.
     */
    public List<S> sources() {
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
    public ImportOptions<S, I> make(final Blueprint<S> blueprint) {
        assign(always(), upgradeBlueprint(blueprint));
        return this;
    }

    /**
     * Creates an {@link Entity} using the specified {@link AdvancedBlueprint} for each source.
     */
    public ImportOptions<S, I> make(final AdvancedBlueprint<S> blueprint) {
        assign(always(), blueprint);
        return this;
    }

    /**
     * Creates {@link Entity entities} using the specified {@link ComplexBlueprint} for each source.
     */
    public ImportOptions<S, I> makeComplex(final ComplexBlueprint<S> blueprint) {
        assign(always(), blueprint);
        return this;
    }

    public ImportOptions<S, I> assign(final I index, final Blueprint<S> blueprint) {
        assign(ImportCondition.index(index), upgradeBlueprint(blueprint));
        return this;
    }

    public ImportOptions<S, I> assign(final I index, final AdvancedBlueprint<S> blueprint) {
        assign(ImportCondition.index(index), blueprint);
        return this;
    }

    public ImportOptions<S, I> assign(final ImportCondition<S, I> condition, final Blueprint<S> blueprint) {
        assign(condition, upgradeBlueprint(blueprint));
        return this;
    }

    public ImportOptions<S, I> assign(final ImportCondition<S, I> condition, final AdvancedBlueprint<S> blueprint) {
        assign(condition, upgradeBlueprint(blueprint));
        return this;
    }

    public ImportOptions<S, I> assignComplex(final I index, final ComplexBlueprint<S> blueprint) {
        assign(ImportCondition.index(index), blueprint);
        return this;
    }

    public ImportOptions<S, I> assign(final ImportCondition<S, I> condition, final ComplexBlueprint<S> blueprint) {
        assignments.add(new ImportAssignment<>(condition, blueprint));
        return this;
    }

    public Function<S, I> indexFunction() {
        return indexFunction;
    }

    public List<ImportAssignment<S, I>> assignments() {
        return Collections.unmodifiableList(assignments);
    }

    private static <S> ComplexBlueprint<S> upgradeBlueprint(final Blueprint<S> blueprint) {
        return (source, context) -> List.of(blueprint.assembleFrom(source));
    }

    private static <S> ComplexBlueprint<S> upgradeBlueprint(final AdvancedBlueprint<S> blueprint) {
        return (source, context) -> List.of(blueprint.assembleFrom(source, context));
    }

}
