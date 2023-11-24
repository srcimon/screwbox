package io.github.srcimon.screwbox.core.entities.components;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.entities.Component;
import io.github.srcimon.screwbox.core.graphics.Flip;
import io.github.srcimon.screwbox.core.graphics.Sprite;

public class RenderComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Sprite sprite;
    public int drawOrder;
    public Percent opacity;
    public double scale = 1;
    public Rotation rotation = Rotation.none();
    public Flip flip = Flip.NONE;

    public RenderComponent() {
        this(0);
    }

    public RenderComponent(final int drawOrder) {
        this(Sprite.invisible(), drawOrder);
    }

    public RenderComponent(final Sprite sprite, final int drawOrder) {
        this(sprite, drawOrder, Percent.max());
    }

    public RenderComponent(final Sprite sprite) {
        this(sprite, 0);
    }

    public RenderComponent(final Sprite sprite, final int drawOrder, final Percent opacity) {
        this.sprite = sprite;
        this.drawOrder = drawOrder;
        this.opacity = opacity;
    }
}
