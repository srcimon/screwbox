package io.github.srcimon.screwbox.pathfinding.components;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Sprite;

public class SpriteChangeComponent implements Component {

    private static final long serialVersionUID = 1L;

    public SpriteChangeComponent(Sprite standing, Sprite walking) {
        this.standing = standing;
        this.walking = walking;
    }

    public Sprite standing;
    public Sprite walking;
}
