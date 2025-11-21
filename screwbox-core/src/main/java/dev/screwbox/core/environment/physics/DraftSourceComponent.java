package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

//TODO document component
//TODO change position in docs
//TODO update particles documentation
//TODO change github issue https://github.com/srcimon/screwbox/issues/846
//TODO Changelog (https://github.com/srcimon/screwbox/issues/846)
public class DraftSourceComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double range;
    public Percent modifier;
    public Vector lastPosition;

    public DraftSourceComponent(final double range) {
        this(range, Percent.half());
    }

    public DraftSourceComponent(final double range, final Percent modifier) {
       this.range = range;
       this.modifier = modifier;
    }
}
