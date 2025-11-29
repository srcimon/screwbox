package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;

import java.io.Serial;
import java.util.function.Supplier;

/**
 * Adds {@link Sprite} rendering to the {@link Entity}. Supports parallax rendering with draw order.
 * Gets processed by {@link RenderSystem}.
 */
public class RenderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The {@link Sprite} that will be rendered.
     */
    public Sprite sprite;

    /**
     * Rendering options used for customizing the drawing.
     */
    public SpriteDrawOptions options;

    /**
     * Specify the multiplier for horizontal parallax.
     */
    public double parallaxX = 1;

    /**
     * Specify the multiplier for vertical parallax.
     */
    public double parallaxY = 1;

    /**
     * Use orthographic sorting for this rendering.
     *
     * @since 3.14.0
     */
    public boolean isSortOrthographic = false;

    public RenderComponent() {
        this(Sprite.invisible());
    }

    public RenderComponent(final Supplier<Sprite> sprite) {
        this(sprite.get());
    }

    public RenderComponent(final int drawOrder) {
        this(Sprite.invisible(), SpriteDrawOptions.originalSize().drawOrder(drawOrder));
    }

    public RenderComponent(final Sprite sprite) {
        this(sprite, SpriteDrawOptions.originalSize());
    }

    public RenderComponent(final Sprite sprite, final int drawOrder) {
        this(sprite, SpriteDrawOptions.originalSize().drawOrder(drawOrder));
    }

    public RenderComponent(final Supplier<Sprite> sprite, final int drawOrder) {
        this(sprite, SpriteDrawOptions.originalSize().drawOrder(drawOrder));
    }

    public RenderComponent(final Supplier<Sprite> sprite, final SpriteDrawOptions options) {
        this(sprite.get(), options);
    }

    public RenderComponent(final Sprite sprite, final SpriteDrawOptions options) {
        this.sprite = sprite;
        this.options = options;
    }

}
