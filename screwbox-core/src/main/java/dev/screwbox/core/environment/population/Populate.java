package dev.screwbox.core.environment.population;

import dev.screwbox.core.Angle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Populate<T> {
    private Function<T, Object> indexFunction = null;
    private List<Asssociate<?>> associates = new ArrayList<>();

    public Populate<T> indexBy(final Function<T, Object> indexFunction) {
        this.indexFunction = indexFunction;
        return this;
    }


    public Populate<T> associate(Asssociate asssociate) {
        this.associates.add(asssociate);
    }
}
