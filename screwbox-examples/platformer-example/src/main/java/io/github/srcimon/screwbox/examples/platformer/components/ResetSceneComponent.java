package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.ecosphere.Component;

public class ResetSceneComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Time atTime;

    public ResetSceneComponent(Time atTime) {
        this.atTime = atTime;
    }

}
