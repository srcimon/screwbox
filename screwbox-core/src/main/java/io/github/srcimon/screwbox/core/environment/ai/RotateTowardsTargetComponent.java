package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class RotateTowardsTargetComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean updateSprite = true;
    public int targetId;

    public RotateTowardsTargetComponent(final int targetId) {
        this.targetId = targetId;
    }
}
