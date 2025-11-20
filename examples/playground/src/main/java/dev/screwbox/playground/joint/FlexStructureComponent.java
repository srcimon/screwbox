package dev.screwbox.playground.joint;

import dev.screwbox.core.environment.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlexStructureComponent implements Component {

    public List<Joint> joints = new ArrayList<>();

    public FlexStructureComponent(final Joint... joints) {
        this.joints.addAll(Arrays.asList(joints));
    }
}
