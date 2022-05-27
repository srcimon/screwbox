package de.suzufa.screwbox.tiled;

import java.util.List;

public interface Extractor<I, O> {

    public List<O> extractFrom(I input);

}
