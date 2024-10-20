package io.github.srcimon.screwbox.core.scenes.animations;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.scenes.Animation;

import static io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions.originalSize;

/**
 * Fades the {@link Screen} to the specified {@link Sprite}.
 */
public class SpriteFadeAnimation implements Animation {

    private final Sprite sprite;

    /**
     * Fades to the specified {@link Sprite}.
     */
    public SpriteFadeAnimation(final Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void draw(final Screen screen, final Percent progress) {
        screen.drawSprite(sprite, Offset.origin(), originalSize().opacity(progress).rotation(screen.rotation().invert()));
    }
}
