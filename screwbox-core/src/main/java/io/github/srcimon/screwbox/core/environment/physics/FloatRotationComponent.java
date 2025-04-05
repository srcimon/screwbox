package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class FloatRotationComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double adjustmentSpeed = 0.2;
}
