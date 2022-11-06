package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.graphics.Flip;
import de.suzufa.screwbox.core.graphics.Sprite;

public class SpriteComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Sprite sprite;
    public int drawOrder;
    public Percent opacity;
    public double scale = 1;
    public Angle rotation = Angle.none();
    public Flip flip = Flip.NONE;

    public SpriteComponent() {
        this(0);
    }

    public SpriteComponent(final int drawOrder) {
        this(Sprite.invisible(), drawOrder);
    }

    public SpriteComponent(final Sprite sprite, final int drawOrder) {
        this(sprite, drawOrder, Percent.max());
    }

    public SpriteComponent(final Sprite sprite) {
        this(sprite, 0);
    }

    public SpriteComponent(final Sprite sprite, final int drawOrder, final Percent opacity) {
        this.sprite = sprite;
        this.drawOrder = drawOrder;
        this.opacity = opacity;
    }
}
