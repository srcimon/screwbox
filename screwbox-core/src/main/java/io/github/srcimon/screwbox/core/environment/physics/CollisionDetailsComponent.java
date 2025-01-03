package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.io.Serial;

//TODO javadoc
//TODO changelog
public class CollisionDetailsComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Entity entityLeft;
    public Entity entityTop;
    public Entity entityRight;
    public Entity entityBottom;

    public boolean touchesLeft;
    public boolean touchesTop;
    public boolean touchesRight;
    public boolean touchesBottom;
}
