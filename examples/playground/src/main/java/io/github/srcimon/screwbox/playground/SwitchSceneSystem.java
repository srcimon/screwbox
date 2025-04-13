package io.github.srcimon.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.filter.MaskImageFilter;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.scenes.Animation;
import dev.screwbox.core.scenes.SceneTransition;

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
