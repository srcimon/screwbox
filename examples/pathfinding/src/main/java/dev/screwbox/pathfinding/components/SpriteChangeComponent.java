package dev.screwbox.pathfinding.components;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Sprite;

import java.io.Serial;

public class SpriteChangeComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public SpriteChangeComponent(Sprite standing, Sprite walking) {
        this.standing = standing;
        this.walking = walking;
    }

    public Sprite standing;
    public Sprite walking;
}
