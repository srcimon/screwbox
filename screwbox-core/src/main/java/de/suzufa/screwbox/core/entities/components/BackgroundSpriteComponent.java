package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Sprite;

public class BackgroundSpriteComponent extends SpriteComponent {

    private static final long serialVersionUID = 1L;

    public BackgroundSpriteComponent(final int drawOrder) {
        super(Sprite.invisible(), drawOrder);
    }

    public BackgroundSpriteComponent(final Sprite sprite, final int drawOrder) {
    	super(sprite, drawOrder, Percentage.max());
    }

    public BackgroundSpriteComponent(final Sprite sprite, final int drawOrder, final Percentage opacity) {
    	super(sprite, drawOrder,opacity);
    }
}
