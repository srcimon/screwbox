package de.suzufa.screwbox.core.resources;

import java.util.List;

public interface Extractor<I, O> {

    public List<O> extractFrom(I input);

}
