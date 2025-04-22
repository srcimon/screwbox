package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;

public class SubmergeComponent implements Component {

    public Double normalDepth;
    public double depth;

    public SubmergeComponent(double depth) {
        this.depth = depth;
    }
}
