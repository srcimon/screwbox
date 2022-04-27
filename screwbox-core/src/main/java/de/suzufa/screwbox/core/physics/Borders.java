package de.suzufa.screwbox.core.physics;

import de.suzufa.screwbox.core.physics.internal.AllBordersExtractionMethod;
import de.suzufa.screwbox.core.physics.internal.ExtractSegmentsMethod;
import de.suzufa.screwbox.core.physics.internal.TopBorderExtractionMethod;
import de.suzufa.screwbox.core.physics.internal.VerticalBordersExtractionMethod;

public enum Borders {

    ALL(new AllBordersExtractionMethod()),
    TOP_ONLY(new TopBorderExtractionMethod()),
    VERTICAL_ONLY(new VerticalBordersExtractionMethod());

    private final ExtractSegmentsMethod method;

    private Borders(final ExtractSegmentsMethod method) {
        this.method = method;
    }

    ExtractSegmentsMethod method() {
        return method;
    }

}
