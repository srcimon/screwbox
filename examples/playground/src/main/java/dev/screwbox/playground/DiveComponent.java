package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class DiveComponent implements Component {


    @Serial
    private static final long serialVersionUID = 1L;

    public Double inactiveDepth;
    public double maxDepth;

    public DiveComponent() {
        this(Double.MAX_VALUE);
    }

    public DiveComponent(double maxDepth) {
        this.maxDepth = maxDepth;
    }
}
