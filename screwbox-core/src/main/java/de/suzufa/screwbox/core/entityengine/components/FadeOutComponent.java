package de.suzufa.screwbox.core.entityengine.components;

import de.suzufa.screwbox.core.entityengine.Component;

public class FadeOutComponent implements Component {

    private static final long serialVersionUID = 1L;
    public final double speed;

    public FadeOutComponent(final double speed) {
        this.speed = speed;
    }
}
