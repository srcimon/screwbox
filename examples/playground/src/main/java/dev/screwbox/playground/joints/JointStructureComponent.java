package dev.screwbox.playground.joints;

import dev.screwbox.core.environment.Component;

import java.util.List;

//TODO environment.enableJoints()
public class JointStructureComponent implements Component {

    public List<Joint> links;
    //TODO Arrays: targetENtityIds, length

    public JointStructureComponent(List<Joint> links) {
        this.links = links;
    }
}
