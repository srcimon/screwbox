package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.List;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Component;

public class AutomovementComponent implements Component {

    private static final long serialVersionUID = 1L;

    // TODO: CONSTRUCTOR
    public double speed = 20;
    public List<Vector> path; // TODO:Path.class
}
