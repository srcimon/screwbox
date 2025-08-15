package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;

import java.util.List;

public class JointComponent implements Component {

    List<Joint> joints;

    public JointComponent(List<Joint> joints) {
        this.joints = joints;
    }
}
