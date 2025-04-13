package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.scenes.Animation;

/**
 * Fades the {@link Screen} to the specified {@link Sprite}.
 */
public class SpriteFadeAnimation implements Animation {

    private final Sprite sprite;
    private final SpriteDrawOptions options;

    /**
     * Fades to the specified {@link Sprite}.
     */
    public SpriteFadeAnimation(final Sprite sprite, final SpriteDrawOptions options) {
        this.sprite = sprite;
        this.options = options;
    }

    @Override
    public void draw(final Canvas canvas, final Percent progress) {
        canvas.drawSprite(sprite, Offset.origin().substract(canvas.offset()), options.opacity(progress));
    }
}
