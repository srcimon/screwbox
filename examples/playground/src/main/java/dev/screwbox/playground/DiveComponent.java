package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;

public class DiveComponent implements Component {

    public Double inactiveDepth;
    public double maxDepth;

    public DiveComponent(double maxDepth) {
        this.maxDepth = maxDepth;
    }
}
