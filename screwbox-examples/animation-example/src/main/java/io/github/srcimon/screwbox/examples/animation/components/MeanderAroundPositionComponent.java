package io.github.srcimon.screwbox.examples.animation.components;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Component;
import io.github.srcimon.screwbox.core.utils.Lurk;

import java.io.Serial;

public class MeanderAroundPositionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Lurk x = Lurk.intervalWithDeviation(Duration.ofSeconds(8), Percent.half());
    public Lurk y = Lurk.intervalWithDeviation(Duration.ofSeconds(8), Percent.half());

    public final Vector origin;
    public final double maxDistance;

    public MeanderAroundPositionComponent(final Vector origin, final double maxDistance) {
        this.origin = origin;
        this.maxDistance = maxDistance;
    }


}
