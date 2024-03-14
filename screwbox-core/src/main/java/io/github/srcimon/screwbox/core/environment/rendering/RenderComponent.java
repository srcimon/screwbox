package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.io.Serial;

public class RenderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Sprite sprite;
    public int drawOrder;
    public SpriteDrawOptions options;

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
        this.options = SpriteDrawOptions.originalSize().opacity(opacity);
    }
}
