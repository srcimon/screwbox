package de.suzufa.screwbox.tiled;

import java.util.List;

public interface Extractor<I> {

    public List<Object> extractFrom(I input);

}
