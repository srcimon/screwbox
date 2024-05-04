package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

public enum SceneTransitionBundle implements AssetBundle<SceneTransition> {
    FADE_OVER_BLACK(SceneTransition
            .extroAnimation(new ExtroAnimation() {
                @Override
                public void draw(Screen screen, Percent value) {
                    screen.fillWith(Color.BLACK.opacity(value));
                }
            })
            .extroDurationMillis(250)
            .extroEase(Ease.SINE_IN)
            .introAnimation(new IntroAnimation() {
                @Override
                public void draw(Screen screen, Percent value, Sprite screenshot) {
                    screen.fillWith(Color.BLACK.opacity(value));
                }
            })
            .introDurationMillis(250)
            .introEase(Ease.SINE_OUT)),
    FADEOUT(SceneTransition.noExtro().

            introAnimation(new IntroAnimation() {
                @Override
                public void draw(Screen screen, Percent value, Sprite screenshot) {
                    screen.drawSprite(screenshot, Offset.origin(), SpriteDrawOptions.originalSize().opacity(value));
                }
            }).introDurationMillis(500)),
    FADEOUT_SLOW(FADEOUT.get().introDurationSeconds(1)),
    SLIDE_UP(SceneTransition.noExtro()
            .introAnimation(new IntroAnimation() {
                @Override
                public void draw(Screen screen, Percent value, Sprite screenshot) {
                    screen.drawSprite(screenshot, Offset.origin().addY((int) (screen.size().height() * -value.invert().value())), SpriteDrawOptions.originalSize());
                }
            }).introDurationSeconds(1));

    private final SceneTransition sceneTransition;

    SceneTransitionBundle(final SceneTransition sceneTransition) {
        this.sceneTransition = sceneTransition;
    }

    @Override
    public Asset<SceneTransition> asset() {
        return Asset.asset(() -> sceneTransition);
    }
}
