package dev.screwbox.core.environment.population;

import java.util.List;
import java.util.function.Function;

public class Populate<T, I> {
    private final List<T> sources;
    private Function<T, I> indexFunction;
    private MatchCriteria<T, I> criteria = MatchCriteria.always();

    public Populate(List<T> sources, Function<T, I> indexFunction) {
        this.indexFunction = indexFunction;
        this.sources = sources;
    }

    public <J> Populate<T, J> indexBy(final Function<T, J> indexFunction) {
        return new Populate<>(sources, indexFunction);
    }

    public Populate<T, I> importRule(MatchCriteria<T, I> criteria) {
        this.criteria = criteria;
        return this;
    }

    public Populate<T, I> assign(EntityBlueprint<T> blueprint) {
        sources.stream()
                .filter(s -> criteria.matches(s, indexFunction.apply(s)))
                .forEach(s -> blueprint.createFrom(s, new ImportContext()));
        return this;
    }
}
