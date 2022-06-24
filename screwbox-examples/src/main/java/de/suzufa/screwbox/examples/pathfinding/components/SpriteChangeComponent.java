package de.suzufa.screwbox.examples.pathfinding.components;

import de.suzufa.screwbox.core.entityengine.Component;
import de.suzufa.screwbox.core.graphics.Sprite;

public class SpriteChangeComponent implements Component {

    private static final long serialVersionUID = 1L;

    public SpriteChangeComponent(Sprite standing, Sprite walking) {
        this.standing = standing;
        this.walking = walking;
    }

    public Sprite standing;
    public Sprite walking;
}
