package dev.screwbox.core.environment.setup;

import java.util.List;
import java.util.function.Function;

public class Setup<T, I> {
    private final List<T> sources;
    private Function<T, I> indexFunction;
    private RuleCriteria<T, I> criteria = RuleCriteria.always();

    public Setup(List<T> sources, Function<T, I> indexFunction) {
        this.indexFunction = indexFunction;
        this.sources = sources;
    }

    public <J> Setup<T, J> indexBy(final Function<T, J> indexFunction) {
        return new Setup<>(sources, indexFunction);
    }

    public Setup<T, I> on(RuleCriteria<T, I> criteria) {
        this.criteria = criteria;
        return this;
    }

    public Setup<T, I> as(ContextAwareBlueprint<T> blueprint) {
        sources.stream()
                .filter(s -> criteria.matches(s, indexFunction.apply(s)))
                .forEach(s -> blueprint.createFrom(s, new ImportContext()));
        return this;
    }

    public Setup<T, I> as(Blueprint<T> blueprint) {
        sources.stream()
                .filter(s -> criteria.matches(s, indexFunction.apply(s)))
                .forEach(s -> blueprint.createFrom(s));
        return this;
    }
}
