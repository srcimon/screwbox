package io.github.srcimon.screwbox.core.scenes.animations;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.scenes.Animation;

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

    //TODO remove screen from interface
    @Override
    public void draw(final Canvas canvas, final Percent progress) {
        canvas.drawSprite(sprite, Offset.origin().substract(canvas.offset()), options.opacity(progress));
    }
}
