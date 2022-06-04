package de.suzufa.screwbox.core.resources;

import java.util.List;

public class EntityExtractor<T> {

    private final T input;

    public static <T> EntityExtractor<T> extractFrom(T input) {
        return new EntityExtractor<>(input);
    }

    private EntityExtractor(T input) {
        this.input = input;
    }

    public <O> EntityExtraction<T, O> extractVia(Extractor<T, O> extractor) {
        List<O> extractedEntities = extractor.extractFrom(input);
        return new EntityExtraction<>(extractedEntities, this);
    }
}
