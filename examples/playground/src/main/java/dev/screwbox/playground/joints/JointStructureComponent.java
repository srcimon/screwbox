package dev.screwbox.playground.joints;

import dev.screwbox.core.environment.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO environment.enableJoints()
public class JointStructureComponent implements Component {

    public List<Joint> joints = new ArrayList<>();

    public JointStructureComponent(final Joint... joints) {
        this.joints.addAll(Arrays.asList(joints));
    }
}
