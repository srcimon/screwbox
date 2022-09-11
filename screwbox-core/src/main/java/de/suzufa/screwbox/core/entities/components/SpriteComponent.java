package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.graphics.FlipMode;
import de.suzufa.screwbox.core.graphics.Sprite;

public class SpriteComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Sprite sprite;
    public int drawOrder;
    public Percentage opacity;
    public double scale = 1;
    public Rotation rotation = Rotation.none();
    public FlipMode flipMode = FlipMode.NONE;

    public SpriteComponent() {
        this(0);
    }

    public SpriteComponent(final int drawOrder) {
        this(Sprite.invisible(), drawOrder);
    }

    public SpriteComponent(final Sprite sprite, final int drawOrder) {
        this(sprite, drawOrder, Percentage.max());
    }

    public SpriteComponent(final Sprite sprite) {
        this(sprite, 0);
    }

    public SpriteComponent(final Sprite sprite, final int drawOrder, final Percentage opacity) {
        this.sprite = sprite;
        this.drawOrder = drawOrder;
        this.opacity = opacity;
    }
}
