package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.scenes.ExtroAnimation;
import io.github.srcimon.screwbox.core.scenes.IntroAnimation;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;

import static io.github.srcimon.screwbox.core.Duration.oneSecond;

public enum SceneTransitionBundle implements AssetBundle<SceneTransition> {
    FADE_OVER_BLACK(SceneTransition
            .extro(new ExtroAnimation() {
                @Override
                public void draw(Screen screen, Percent value) {
                    screen.fillWith(Color.BLACK.opacity(value));
                }
            }, Duration.ofMillis(250))
            .extroTweenMode(TweenMode.SINE_IN)
            .intro(new IntroAnimation() {
                @Override
                public void draw(Screen screen, Percent value, Sprite screenshot) {
                    screen.fillWith(Color.BLACK.opacity(value));
                }
            }, Duration.ofMillis(250))
            .introTweenMode(TweenMode.SINE_OUT)),
    FADEOUT(SceneTransition.noExtro().

            intro(new IntroAnimation() {
                @Override
                public void draw(Screen screen, Percent value, Sprite screenshot) {
                    screen.drawSprite(screenshot, Offset.origin(), SpriteDrawOptions.originalSize().opacity(value));
                }
            }, Duration.ofMillis(500))),
    FADEOUT_SLOW(FADEOUT.get().introDuration(oneSecond())),
    SLIDE_UP(SceneTransition.noExtro()
            .intro(new IntroAnimation() {
                @Override
                public void draw(Screen screen, Percent value, Sprite screenshot) {
                    screen.drawSprite(screenshot, Offset.origin().addY((int) (screen.size().height() * -value.invert().value())), SpriteDrawOptions.originalSize());
                }
            }, Duration.ofMillis(1000)));//TODO simplify duration changes

    private final SceneTransition sceneTransition;

    SceneTransitionBundle(final SceneTransition sceneTransition) {
        this.sceneTransition = sceneTransition;
    }

    @Override
    public Asset<SceneTransition> asset() {
        return Asset.asset(() -> sceneTransition);
    }
}
