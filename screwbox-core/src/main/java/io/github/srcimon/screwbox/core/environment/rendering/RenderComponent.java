package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.io.Serial;
import java.util.function.Supplier;

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

    public RenderComponent(final Supplier<Sprite> sprite, final int drawOrder) {
        this(sprite.get(), drawOrder, Percent.max());
    }

    public RenderComponent(final Sprite sprite, final int drawOrder) {
        this(sprite, drawOrder, Percent.max());
    }

    public RenderComponent(final Supplier<Sprite> sprite) {
        this(sprite.get(), 0);
    }

    public RenderComponent(final Sprite sprite) {
        this(sprite, 0);
    }

    //TODO remove
    public RenderComponent(final Sprite sprite, final int drawOrder, final Percent opacity) {
        this.sprite = sprite;
        this.drawOrder = drawOrder;
        this.options = SpriteDrawOptions.originalSize().opacity(opacity);
    }

    public RenderComponent(final Supplier<Sprite> sprite, final SpriteDrawOptions options) {
        this(sprite.get(), options);
    }

    public RenderComponent(final Sprite sprite, final SpriteDrawOptions options) {
        this.sprite = sprite;
        this.options = options;
    }

    public RenderComponent(final Supplier<Sprite> sprite, final int drawOrder, final SpriteDrawOptions options) {
        this(sprite.get(), drawOrder, options);
    }

    public RenderComponent(final Sprite sprite, final int drawOrder, final SpriteDrawOptions options) {
        this.sprite = sprite;
        this.drawOrder = drawOrder;
        this.options = options;
    }
}
