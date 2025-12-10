package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;

public class SoftBodyPreasureComponent implements Component {

    public double preasure;

    public SoftBodyPreasureComponent(final double preasure) {
        this.preasure = preasure;
    }
}
