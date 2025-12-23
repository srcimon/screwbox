package dev.screwbox.core.environment.population;

import dev.screwbox.core.utils.TileMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Populate<T> {
    private Function<T, Object> indexFunction = null;
    private List<Associate<?>> associates = new ArrayList<>();

    public Populate<T> useIndex(final Function<T, Object> indexFunction) {
        this.indexFunction = indexFunction;
        return this;
    }


    public Populate<T> associate(Associate<T> asssociate) {
        this.associates.add(asssociate);
        return this;
    }
}
