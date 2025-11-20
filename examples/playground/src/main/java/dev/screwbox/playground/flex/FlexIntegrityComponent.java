package dev.screwbox.playground.flex;

import dev.screwbox.core.environment.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO environment.enableFlexPhysics()
public class FlexIntegrityComponent implements Component {

    public List<Joint> joints = new ArrayList<>();

    public FlexIntegrityComponent(final Joint... joints) {
        this.joints.addAll(Arrays.asList(joints));
    }
}
