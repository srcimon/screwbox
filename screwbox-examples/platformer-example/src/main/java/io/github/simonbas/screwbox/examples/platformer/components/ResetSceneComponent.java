package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.entities.Component;

public class ResetSceneComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Time atTime;

    public ResetSceneComponent(Time atTime) {
        this.atTime = atTime;
    }

}
