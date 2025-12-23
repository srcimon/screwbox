package dev.screwbox.core.environment.setup;

import java.util.List;
import java.util.function.Function;

public class Populate<T, I> {
    private final List<T> sources;
    private Function<T, I> indexFunction;
    private RuleCriteria<T, I> criteria = RuleCriteria.always();

    public Populate(List<T> sources, Function<T, I> indexFunction) {
        this.indexFunction = indexFunction;
        this.sources = sources;
    }

    public <J> Populate<T, J> indexBy(final Function<T, J> indexFunction) {
        return new Populate<>(sources, indexFunction);
    }

    public Populate<T, I> where(RuleCriteria<T, I> criteria) {
        this.criteria = criteria;
        return this;
    }

    public Populate<T, I> useBlueprint(ContextAwareBlueprint<T> blueprint) {
        sources.stream()
                .filter(s -> criteria.matches(s, indexFunction.apply(s)))
                .forEach(s -> blueprint.createFrom(s, new ImportContext()));
        return this;
    }

    public Populate<T, I> useBlueprint(Blueprint<T> blueprint) {
        sources.stream()
                .filter(s -> criteria.matches(s, indexFunction.apply(s)))
                .forEach(s -> blueprint.createFrom(s));
        return this;
    }
}
