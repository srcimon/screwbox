package dev.screwbox.core.environment.population;

import java.util.function.Function;

public class Populate<T, I> {
    private Function<T, I> indexFunction;

    public Populate(Function<T, I> indexFunction) {
        this.indexFunction = indexFunction;
    }

    public <J> Populate<T, J> useIndex(final Function<T, J> indexFunction) {
        return new Populate<>(indexFunction);
    }


    public Populate<T, I> associate(Condition<T, I> association) {
        return this;
    }

    public Populate<T, I> withBlueprint(EntityBlueprint<T> blueprint) {
        return this;
    }
}
