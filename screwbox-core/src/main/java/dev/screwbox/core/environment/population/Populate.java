package dev.screwbox.core.environment.population;

import java.util.function.Function;

public class Populate<T> {
    private Function<T, Object> indexFunction = null;

    public void indexBy(final Function<T, Object> indexFunction) {
        this.indexFunction = indexFunction;
    }


}
