package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.util.function.Supplier;

public enum IntroAnimationBundle implements AssetBundle<IntroAnimation> {

    FADE_FROM_BLACK(() -> (screen, value, screenshot) ->
            screen.fillWith(Color.BLACK.opacity(value))),
    FADE_OUT(() -> (screen, value, screenshot) ->
            screen.drawSprite(screenshot, Offset.origin(), SpriteDrawOptions.originalSize().opacity(value))),
    SLIDE_UP(() -> (screen, value, screenshot) ->
            screen.drawSprite(
                    screenshot,
                    Offset.origin().addY((int) (screen.size().height() * -value.invert().value())),
                    SpriteDrawOptions.originalSize()));

    private final Asset<IntroAnimation> animation;

    IntroAnimationBundle(final Supplier<IntroAnimation> animation) {
        this.animation = Asset.asset(animation);
    }

    @Override
    public Asset<IntroAnimation> asset() {
        return animation;
    }
}
