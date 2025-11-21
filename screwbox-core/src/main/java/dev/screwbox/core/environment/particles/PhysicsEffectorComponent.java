package dev.screwbox.core.environment.particles;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

//TODO document component
//TODO change position in docs
//TODO update particles documentation
//TODO change github issue https://github.com/srcimon/screwbox/issues/846
//TODO Changelog (https://github.com/srcimon/screwbox/issues/846)
public class PhysicsEffectorComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double range;
    public Percent modifier;
    public Vector lastPosition;

    public PhysicsEffectorComponent(final double range) {
        this(range, Percent.half());
    }

    public PhysicsEffectorComponent(final double range, final Percent modifier) {
       this.range = range;
       this.modifier = modifier;
    }
}
