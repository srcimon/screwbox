package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.graphics.internal.filter.MaskImageFilter;
import io.github.srcimon.screwbox.core.graphics.options.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.Animation;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;

import java.awt.*;

public class SwitchSceneSystem implements EntitySystem {

    static Shader shader = new Shader("nocache") {
        @Override
        public Image apply(Image source, Percent progress) {
            return ImageOperations.applyFilter(source, new MaskImageFilter(ANIMATION.get().singleFrame(), progress.rangeValue(0, 255), true));
        }
    };

    private static final Asset<Sprite> BLACK = Asset.asset(() -> Sprite.fromFile("black.png").prepareShader(shader));
    private static final Asset<Sprite> ANIMATION = Asset.asset(() -> Sprite.fromFile("transition.png"));

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.ESCAPE)) {
            Animation outroAnimation = (canvas, progress) -> canvas.drawSprite(BLACK, Offset.origin(), SpriteDrawOptions.scaled(2)
                    .shaderSetup(ShaderSetup.shader(shader).progress(progress.invert())));
            engine.scenes().resetActiveScene(SceneTransition.custom()
                    .outroAnimation(outroAnimation).outroDurationSeconds(2)
                    .introAnimation(outroAnimation).introDurationSeconds(2)
            );
        }
    }
}
