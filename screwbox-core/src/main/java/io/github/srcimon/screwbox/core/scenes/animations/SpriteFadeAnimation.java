package io.github.srcimon.screwbox.core.scenes.animations;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.scenes.Animation;

import static io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions.originalSize;

public class SpriteFadeAnimation implements Animation {

    private final Sprite sprite;

    public SpriteFadeAnimation(final Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void draw(final Screen screen, final Percent progress) {
        screen.drawSprite(sprite, Offset.origin(), originalSize().opacity(progress));
    }
}
