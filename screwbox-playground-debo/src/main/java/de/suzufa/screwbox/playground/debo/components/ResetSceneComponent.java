package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Component;

public class ResetSceneComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Time atTime;

    public ResetSceneComponent(Time atTime) {
        this.atTime = atTime;
    }

}
