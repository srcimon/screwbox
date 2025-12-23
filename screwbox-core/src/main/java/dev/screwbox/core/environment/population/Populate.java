package dev.screwbox.core.environment.population;

import java.util.function.Function;

public class Populate<T, I> {
    private Function<T, I> indexFunction;

    public Populate(Function<T, I> indexFunction) {
        this.indexFunction = indexFunction;
    }

    public <J> Populate<T, J> indexBy(final Function<T, J> indexFunction) {
        return new Populate<>(indexFunction);
    }


    public Populate<T, I> when(MatchCriteria<T, I> association) {
        return this;
    }

    public Populate<T, I> assignBlueprint(EntityBlueprint<T> blueprint) {
        return this;
    }
}
